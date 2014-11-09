package com.lunex.timeseries.element;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class AbstractDataElement implements DataElement {

  transient static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

  long time;
  int weight;

  @Override
  public long getTime() {
    return time;
  }

  @Override
  public void setTime(long time) {
    this.time = time;
  }

  @Override
  public long getWeight() {
    return weight;
  }

}
