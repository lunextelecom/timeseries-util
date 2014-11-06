package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

public interface TimeDataset<T extends DataElement> extends TimeEventListener{


  public enum AggregateType {
    min, max, avg, count, ohlc
  }


  /**
   * Unique name to identify this series
   * @return
   */
  String getKey();

  /**
   * Used during creation to initialize
   * @param time
   */
  void initTime(long time);

  int getBucketSize();

  T getData(long time);

  T current();

  T last();

  AggregateType getAggregateType();

}
