package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;


//todo implement this class
public class TimeSeriesBucket<T extends DataElement> implements TimeDataset<T>, TimeEventListener {

  String key;
  int bucketSize;
  T item;
  AggregateType type;

  public TimeSeriesBucket(String key, int bucketSize) {
    this(key, bucketSize, AggregateType.avg);
  }

  public TimeSeriesBucket(String key, int bucketSize, AggregateType type) {
    this.key = key;
    this.bucketSize = bucketSize;
    this.type = type;
  }

  /**
   * Unique name to identify this series
   */
  @Override
  public void initTime(long time) {

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
    return item;
  }

  @Override
  public T current() {
    return item;
  }

  @Override
  public T last() {
    return item;
  }

  @Override
  public AggregateType getAggregateType() {
    return type;
  }

  /**
   * @param time  - the time generate by the sender, it might not necessary be the time of the
   *              event
   * @param event - the time Event object
   */
  @Override
  public boolean onEvent(long time, TimeEvent event) {
    return false;
  }
}
