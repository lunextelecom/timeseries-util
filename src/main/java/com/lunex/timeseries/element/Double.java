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
  public void subtract(DataElement remove, TimeDataset.AggregateType type) {
    Double _item = (Double)remove;
    switch (type){
      case min:
        this.value = Math.max(_item.value, this.value);
        break;
      case max:
        this.value = Math.min(_item.value, this.value);
        break;
      case count:
        this.value -= _item.value;
        break;
      case avg:
      default:
        this.value = (this.value * this.weight - _item.value * _item.weight)/(this.weight - _item.weight);

    }
    this.weight -= _item.weight;
  }



  @Override
  public void add(DataElement item, TimeDataset.AggregateType type) {
    Double _item = (Double)item;
    switch (type){
      case min:
        this.value = Math.min(_item.value, this.value);
        break;
      case max:
        this.value = Math.max(_item.value, this.value);
        break;
      case count:
        this.value += _item.value;
        break;
      case avg:
      default:
//        if (this.weight + _item.weight == 0) break;
        this.value = (this.value * this.weight + _item.value * _item.weight)/(this.weight + _item.weight);

    }
    this.weight += _item.weight;
  }

  public String toString() {
    return String.format("%s %10.2f %6d", fmt.print(getTime()), value, weight);
  }

}
