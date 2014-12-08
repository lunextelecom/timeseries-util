package com.lunex.timeseries.element;

import com.lunex.timeseries.TimeDataset;
import com.lunex.timeseries.TimeEvent;

public interface DataElement extends Comparable{
  long getTime();
  void setTime(long time);
  double castDouble();
  long getWeight();
  void update(TimeEvent event);
  void add(DataElement item, TimeDataset.AggregateType type);
  void subtract(DataElement item, TimeDataset.AggregateType type);
}
