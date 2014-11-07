package com.lunex.timeseries.element;

import com.lunex.timeseries.TimeDataset;
import com.lunex.timeseries.TimeEvent;

public class Double extends AbstractDataElement implements DataElement {

  double value;

  public Double(){};

  public Double(long time, double value) {
    this(time, value, 1);
  }

  public Double(long time, double value, int weight) {
    this.time = time;
    this.value = value;
    this.weight = weight;
  }

  public double value() {
    return value;
  }

  @Override
  public void update(TimeEvent event) {
    value += event.getValue();
    weight++;
  }

  @Override
  public void add(DataElement item, TimeDataset.AggregateType type) {

  }

  @Override
  public void remove(DataElement item, TimeDataset.AggregateType type) {

  }

  public String toString() {
    return String.format("%s %10d %10d", fmt.print(getTime()), value, weight);
  }

}
