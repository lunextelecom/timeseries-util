package com.lunex.timeseries.element;

import com.lunex.timeseries.TimeEvent;

public class Int extends AbstractDataElement implements DataElement {

  int value;

  public Int(){};

  public Int(long time, int value) {
    this(time, value, 1);
  }

  public Int(long time, int value, int weight) {
    this.time = time;
    this.value = value;
    this.weight = weight;
  }

  public int value() {
    return value;
  }

  @Override
  public void update(TimeEvent event) {
    value += event.getValue();
    weight++;
  }

  public String toString() {
    return String.format("%s %10d %10d", fmt.print(getTime()), value, weight);
  }

}
