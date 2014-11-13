package com.lunex.timeseries;

import com.google.common.reflect.TypeToken;

import com.lunex.timeseries.element.DataElement;

import org.joda.time.DateTimeZone;


/**
 * Provide basic function for subclass
 * @param <T>
 */
public class TimeSeriesBase<T extends DataElement> {

  //automatic determine the class if setClass is not set
  TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
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
    return TimeSeriesUtil.truncate(time, elementSize, TimeSeriesUtil.getDefaultTimeZone());
  }

  public void setClass(Class klass){
    this.klass = klass;
  }

}
