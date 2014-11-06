package com.lunex.timeseries;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DataMap provide a map of how flow of data are connected. 1. mapping of event to TimeData 2.
 * mapping of subscriber to TimeData
 */
public class DataMap {

  //evtName,
  private Map<String, Map<TimeDataset, List<TimeDatasetListener>>>
      seriesByEvtName =
      new LinkedHashMap<String, Map<TimeDataset, List<TimeDatasetListener>>>();

  public Map<TimeDataset, List<TimeDatasetListener>> getEventListeners(String evtName) {
    Map<TimeDataset, List<TimeDatasetListener>> ret = seriesByEvtName.get(evtName);
    if (ret == null) {
      ret = new LinkedHashMap<TimeDataset, List<TimeDatasetListener>>();
      seriesByEvtName.put(evtName, ret);
    }
    return ret;
  }

  /**
   * this makes the time data and the event bind together.  All events of that eventname will now
   * also update the data.  Multiple data can build to the same eventName
   */
  public void addTimeEventListener(String evtName, TimeDataset data) {
    Map<TimeDataset, List<TimeDatasetListener>> listeners = getEventListeners(evtName);
    listeners.put(data, new ArrayList<TimeDatasetListener>());

  }


  public void addDatasetListener(TimeDataset dataset, TimeDatasetListener subscriber) {
    List<TimeDatasetListener> subscribers = getDatasetListener(dataset);
    if (subscriber == null){
      throw new NoSuchElementException(String.format("Dataset %s does not exist", dataset.getKey()));
    }

    if (!subscribers.contains(subscriber)) {
      subscribers.add(subscriber);
    }
  }


  public List<TimeDatasetListener> getDatasetListener(TimeDataset dataset) {
    List<TimeDatasetListener> subscribers = null;
    //todo switch to lambda when moving to java8
    for (Map<TimeDataset, List<TimeDatasetListener>> item : seriesByEvtName.values()) {
      subscribers = item.get(dataset);
      if (subscribers != null) {
        break;
      }
    }

    return subscribers;
  }

}

