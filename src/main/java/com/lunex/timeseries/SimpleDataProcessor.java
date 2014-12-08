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
  public void registerEvent(String evtName, TimeEventObserver listener) {
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
  public int onEvent(long time, TimeEvent event) {
    /**
     * 1. find and pass all TimeEventListener for this event
     * 2. update TimeEventLister(bucket, series)
     * 3. dispatch to any subscriber of those data on new entry added
     */
    DataMap map = getDataMap();

    ArrayList<Tuple> dispatchBuffer = new ArrayList<Tuple>();

    List<TimeEventObserver> hier = map.getEventListeners(event.getName());

    for (TimeEventObserver series : hier) {
      int numItemAdd = series.onEvent(time, event);
      if (numItemAdd>0) {
        if (series instanceof TimeDataset)
          dispatchBuffer.add(new Tuple(series.getKey(), (TimeDataset) series, ((TimeDataset)series).last(), ((TimeSeriesBase)series).getObservers(), numItemAdd));
      }
    }

    for (Tuple item : dispatchBuffer) {
      for (TimeDatasetObserver subscriber : item.subscribers) {
        subscriber.onData(item.data, item.item, item.num);
      }
    }
    return 0;
  }




  class Tuple {

    TimeDataset data;
    String name;
    DataElement item;
    List<TimeDatasetObserver> subscribers;
    int num;


    Tuple(String name, TimeDataset data, DataElement item, List<TimeDatasetObserver> subscribers, int num) {
      this.name = name;
      this.data = data;
      this.item = item;
      this.subscribers = subscribers;
      this.num = num;
    }
  }
}

