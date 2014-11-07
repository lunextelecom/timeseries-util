package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * rolling version of
 *
 * [      60min            ] #bucket [element1]
 * [element2]...[element361][current] #series
 *
 * Notice element1 is kept so we can access the first item is the removed item.
 */
public class TimeSeriesBucket<T extends DataElement> extends TimeSeriesBase<T>
    implements TimeDataset<T> {

  private final Logger log = LoggerFactory.getLogger(TimeSeriesBucket.class);
  String key;
  int bucketSize;
  T bucket;
  AggregateType type;
  TimeSeries<T> series;
  int seriesSize;


  public TimeSeriesBucket(String key, int bucketSize, int elementSize, AggregateType type) {
    this.key = key;
    this.bucketSize = bucketSize;
    this.type = type;
    if (bucketSize % elementSize != 0) {
      throw new RuntimeException("Bucket size must be divible of elementsize");
    }
    //added one so the first element is the
    this.seriesSize = bucketSize / elementSize;
    this.series =
        new TimeSeries<T>(key + "." + "fine", elementSize, type, seriesSize + 1);
  }

  /**
   * Unique name to identify this series
   */
  @Override
  public void init(long time) {
    bucket = makeElement();
    bucket.init(time);
    series.init(time);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public T getData(long time) {
    return bucket;
  }

  @Override
  public T first() {
    return bucket;
  }

  @Override
  public T current() {
    return bucket;
  }

  @Override
  public T last() {
    return bucket;
  }

  /**
   * @param time  - the time generate by the sender, it might not necessary be the time of the
   *              event
   * @param event - the time Event object
   */
  @Override
  public boolean onEvent(long time, TimeEvent event) {
    if (series.onEvent(time, event)) {
      //new element is added and element might be removed.
      if (series.size() >= seriesSize + 2) {
        //need to get first element
        //get last element
        bucket.remove(series.first(), getAggregateType());
        bucket.add(series.last(), getAggregateType());
        //set to the new time to the 2nd item
        bucket.setTime(series.list.get(1).getTime());
      } else {
        bucket.add(last(), getAggregateType());
      }
    }
    return false;
  }
}
