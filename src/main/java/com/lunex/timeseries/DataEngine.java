package com.lunex.timeseries;

/**
 * Provide contact point for user to update events and map.
 *
 */
public class DataEngine implements TimeEventListener {

  private DataProcessor processor;

  private DataMap map = new DataMap();


  public DataEngine(DataProcessor processor) {
    this.processor = processor;
    processor.setDataMap(map);
  }

  public DataMap getMap() {
    return map;
  }

  @Override
  public String getKey() {
    return this.getClass().getSimpleName();
  }

  @Override
  public boolean onEvent(long time, TimeEvent event) {
    processor.onEvent(time, event);
    return false;
  }

  public void addDatasetListener(TimeDataset dataset, TimeDatasetListener listener){
    map.addDatasetListener(dataset, listener);
  }

  /**
   * Bind an evtname to the listeners.  TimeEventLister are can be TimeDataset
   * @param evtName
   * @param dataset
   */
  public void bindSeriesToEvent(String evtName, TimeDataset dataset){
    map.addTimeEventListener(evtName, dataset);
    processor.registerEvent(evtName, dataset);
  }
}
