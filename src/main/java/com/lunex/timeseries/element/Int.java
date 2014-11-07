package com.lunex.timeseries.element;

import com.lunex.timeseries.TimeDataset;
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


  @Override
  public void remove(DataElement remove, TimeDataset.AggregateType type) {
    Int _item = (Int)remove;
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
    Int _item = (Int)item;
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
        this.value = (this.value * this.weight + _item.value * _item.weight)/(this.weight + _item.weight);

    }
    this.weight += _item.weight;
  }

  public String toString() {
    return String.format("%s %10d %10d", fmt.print(getTime()), value, weight);
  }

}
