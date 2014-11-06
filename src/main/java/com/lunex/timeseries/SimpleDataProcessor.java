package com.lunex.timeseries;


import com.lunex.timeseries.element.DataElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple processor, use the same thread of the call of processEvent to process and send event
 */
public class SimpleDataProcessor extends DataProcessor {


  @Override
  public void registerEvent(String evtName, TimeEventListener listener) {
    //can use this function to do optimization
  }

  @Override
  public String getKey() {
    return getClass().getSimpleName();
  }

  /**
   * @param time  - the time generate by the sender, it might not necessary be the time of the
   *              event
   * @param event - the time Event object
   */
  @Override
  public boolean onEvent(long time, TimeEvent event) {
    /**
     * 1. find and pass all TimeEventListener for this event
     * 2. update TimeEventLister(bucket, series)
     * 3. dispatch to any subscriber of those data on new entry added
     */
    DataMap map = getDataMap();

    ArrayList<Tuple> dispatchBuffer = new ArrayList<Tuple>();

    Map<TimeDataset, List<TimeDatasetListener>> hier = map.getEventListeners(event.getKey());
    for (Map.Entry<TimeDataset,List<TimeDatasetListener>> entry : hier.entrySet()) {
      TimeDataset series = entry.getKey();
      boolean isNewItem = series.onEvent(time, event);
      if (isNewItem) {
        dispatchBuffer.add(new Tuple(series.getKey(), series, series.last(), entry.getValue()));
      }
    }

    for (Tuple item : dispatchBuffer) {
      for (TimeDatasetListener subscriber : item.subscribers) {
        subscriber.onData(item.data, item.item);
      }
    }
    return false;
  }




  class Tuple {

    TimeDataset data;
    String name;
    DataElement item;
    List<TimeDatasetListener> subscribers;


    Tuple(String name, TimeDataset data, DataElement item, List<TimeDatasetListener> subscribers) {
      this.name = name;
      this.data = data;
      this.item = item;
      this.subscribers = subscribers;
    }
  }
}

