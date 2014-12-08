package com.lunex.timeseries;

import com.google.common.reflect.TypeToken;

import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.ElementFactory;

import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;


/**
 * Provide basic function for subclass
 * @param <T>
 */
public class TimeSeriesBase<T extends DataElement> implements ElementFactory<T>, TimeDatasetObservable {

  //automatic determine the class if setClass is not set
  TypeToken<T> typeToken = new TypeToken<T>(getClass()) {};
  Class<T> klass = (Class<T>) typeToken.getRawType();
  //series information
  String key;
  int elementSize; //in millis
  TimeDataset.AggregateType type;
  List<TimeDatasetObserver> subscribers = new ArrayList<TimeDatasetObserver>();

  /**
   * Generic factory method to create new element to makeElement this class, overwrite for better
   * performance
   */
  public T makeElement() {
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

  @Override
  public void register(TimeDatasetObserver observer) {
    if (!subscribers.contains(observer))
      subscribers.add(observer);
  }

  @Override
  public List<TimeDatasetObserver> getObservers() {
    return subscribers;
  }
}
