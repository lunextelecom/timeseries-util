package com.lunex.timeseries;

import com.google.common.reflect.TypeToken;

import com.lunex.timeseries.element.DataElement;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * In-memory version of timeseries, whatever added will just be in the Array and will not get expired out.
 * Do not use this if you plan to keep adding records for long period of time.
 * Use TimeSeriesRolling instead
 * @param <T>
 */
public class TimeSeries<T extends DataElement> implements TimeDataset<T>, Iterable<T> {
  private final Logger log = LoggerFactory.getLogger(TimeSeries.class);
  transient static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

  TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
  Class<T> klass = (Class<T>)typeToken.getRawType();
  String key;
  int bucketSize;
  AggregateType type;
  //todo internal storage better implementation
  List<T> list = new ArrayList();
  T current = null;
  T last = null;

  public TimeSeries(String key, int bucketSize) {
    this(key, bucketSize, AggregateType.avg);
  }

  public TimeSeries(String key, int bucketSize, AggregateType type) {
    log.debug("TimeSeries {} created", key);
    this.key = key;
    this.bucketSize = bucketSize;
    this.type = type;
  }

  public boolean onEvent(long time, TimeEvent event) {
    long ttime = truncate(event.getTime());
    if ((time - current.getTime() >= bucketSize)) {
      last = current;
      current = makeElement();
      current.init(ttime);
      list.add(current);
      return true;
    }

    current.update(event);
    return false;
  }

  public long truncate(long time){
    return (time / bucketSize ) * bucketSize;
  }

  /**
   * Generic factory method to create new element to makeElement this class, overwrite for better performance
   * @return
   */
  protected T makeElement() {
    try {
      T item = klass.newInstance();
      return item;
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Unique name to identify this series
   */
  @Override
  public void initTime(long time) {
    current = makeElement();
    current.init(truncate(time));
    list.add(current);
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public int getBucketSize() {
    return bucketSize;
  }

  @Override
  public T getData(long time) {
    //todo need better implementation
    //truncate time
    for (T item : list) {
      if (item.getTime() >= time) {
        return item;
      }
    }
    return null;
  }

  @Override
  public T current() {
    return current;
  }

  @Override
  public T last() {
    return last;
  }

  @Override
  public AggregateType getAggregateType() {
    return type;
  }

  @Override
  public Iterator<T> iterator() {
    return list.iterator();
  }

}
