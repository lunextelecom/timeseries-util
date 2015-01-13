package com.lunex.timeseries;

import com.lunex.timeseries.function.OneToOne;

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

  /**
   * Apply a function to a timeseries.
   * @param func
   * @param dataset
   * @param nameOut
   */
  public static void applyFunc(final OneToOne func, final TimeDatasetObservable dataset, String nameOut ){


    if (dataset instanceof TimeSeriesKeyMap) {
      //create FuncWrapper on new series create
      //register newly created Timeseries so it can be obtain later on.

    }
  }

  public TimeDataset getDataset(String name){
    return null;
  }

}
