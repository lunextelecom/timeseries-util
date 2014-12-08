package com.lunex.timeseries;

/**
 * Provide contact point for user to update events and map.
 *
 */
public class DataEngine implements TimeEventObserver {

  private DataProcessor processor;

  private DataMap map = new DataMap();


  public DataEngine(DataProcessor processor) {
    this.processor = processor;
    processor.setDataMap(map);
  }

//  public DataMap getMap() {
//    return map;
//  }

  @Override
  public String getKey() {
    return this.getClass().getSimpleName();
  }

  @Override
  public int onEvent(long time, TimeEvent event) {
    processor.onEvent(time, event);
    return 0;
  }

  public void addDatasetListener(TimeDatasetObservable dataset, TimeDatasetObserver listener){
//    map.addDatasetListener(dataset, listener);
    dataset.register(listener);
  }

  /**
   * Bind an evtname to the listeners.  TimeEventLister are can be TimeDataset
   * @param evtName
   * @param dataset
   */
  public void bindSeriesToEvent(String evtName, TimeEventObserver dataset){
    map.addTimeEventListener(evtName, dataset);
    processor.registerEvent(evtName, dataset);
  }
}
