package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * In-memory version of timeseries, whatever added will just be in the Array and will not get
 * expired out if seriesSize is not set. To expired old bucket, set seriesSize to have fix memory usage.
 */
public class TimeSeries<T extends DataElement> extends TimeSeriesBase<T> implements TimeDataset<T>, Iterable<T> {

  private final Logger log = LoggerFactory.getLogger(TimeSeries.class);

  private List<T> list = new ArrayList();
  private T current = null;
  private T last = null;

  private int seriesSize = -1; //set this to a fix number to prevent over memory usage. default t0 100000

  public TimeSeries(String key, int elementSize) {
    this(key, elementSize, AggregateType.avg, 100000);
  }

  public TimeSeries(String key, int elementSize, AggregateType type, int seriesSize) {
    log.debug("TimeSeries {} created", key);
    this.key = key;
    this.elementSize = elementSize;
    this.type = type;
    this.seriesSize = seriesSize;
  }

  public boolean onEvent(long time, TimeEvent event) {

    if ((time - current.getTime() >= elementSize)) {

      last = current;
      current = makeElement();
      current.setTime(truncate(event.getTime()));
      list.add(current);

      if (seriesSize != -1) {
        while (first().getTime() < ) {
          list.remove(0);
        }
      }
      current.update(event);
      return true;
    } else {
      current.update(event);
      return false;
    }
  }


  @Override
  public void init(long time) {
    current = makeElement();
    current.setTime(truncate(time));
    list.add(current);
  }

  @Override
  public int size() {
    return list.size();
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
  public T first() {
    return list.get(0);
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
  public Iterator<T> iterator() {
    return list.iterator();
  }

}
