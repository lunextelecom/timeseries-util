package com.lunex.timeseries;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DataMap provide a map of how flow of data are connected. 1. mapping of event to TimeData 2.
 * mapping of subscriber to TimeData
 */
public class DataMap implements DatasetCreateObserver{

  //evtName,
  private Map<String, List<TimeEventObserver>>
      seriesByEvtName =
      new LinkedHashMap<String, List<TimeEventObserver>>();

  public List<TimeEventObserver> getEventListeners(String evtName) {
    List<TimeEventObserver> ret = seriesByEvtName.get(evtName);
    if (ret == null) {
      ret = new ArrayList<TimeEventObserver>();
      seriesByEvtName.put(evtName, ret);
    }
    return ret;
  }

  //Global list of all series by name
  public Map<String, TimeDataset> seriesByName = new LinkedHashMap<String, TimeDataset>();

  /**
   * this makes the time data and the event bind together.  All events of that eventname will now
   * also update the data.  Multiple data can build to the same eventName
   */
  public void addTimeEventListener(String evtName, TimeEventObserver data) {
    List<TimeEventObserver> listeners = getEventListeners(evtName);
    if (!listeners.contains(data))
      listeners.add(data);

  }

  @Override
  public void onSeriesCreate(TimeSeries series) {
    seriesByName.put(series.getKey(), series);
  }
}

