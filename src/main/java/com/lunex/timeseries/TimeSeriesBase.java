package com.lunex.timeseries;

import com.google.common.reflect.TypeToken;

import com.lunex.timeseries.element.DataElement;

public class TimeSeriesBase<T extends DataElement> {

  TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
  };
  Class<T> klass = (Class<T>) typeToken.getRawType();
  //series information
  String key;
  int elementSize; //in millis
  TimeDataset.AggregateType type;


  /**
   * Generic factory method to create new element to makeElement this class, overwrite for better
   * performance
   */
  protected T makeElement() {
    try {
      T item = klass.newInstance();
      return item;
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public String getKey() {
    return key;
  }

  public int getElementSize() {
    return elementSize;
  }

  public TimeDataset.AggregateType getAggregateType() {
    return type;
  }

  public long truncate(long time) {
    return (time / elementSize) * elementSize;
  }

}
