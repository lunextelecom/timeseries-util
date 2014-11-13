package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

public interface TimeDataset<T extends DataElement> extends TimeEventListener {


  public enum AggregateType {
    min, max, avg, count
  }

  /**
   * Unique name to identify this series
   */
  String getKey();

  /**
   * Used during creation to initialize
   */
  void init(long time);

  int getElementSize();

  int size();

  T getData(long time);

  T first();

  T current();

  T last();


  AggregateType getAggregateType();



}
