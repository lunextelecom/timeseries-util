package com.lunex.timeseries.element;

import com.lunex.timeseries.TimeEvent;

public interface DataElement {
  void init(long time);
  long getTime();
  long getWeight();
  void update(TimeEvent event);
}
