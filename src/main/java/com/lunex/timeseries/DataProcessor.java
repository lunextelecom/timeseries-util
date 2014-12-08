package com.lunex.timeseries;

public abstract class DataProcessor implements TimeEventObserver {

  private DataMap dataMap;

  public DataMap getDataMap() {
    return dataMap;
  }

  public void setDataMap(DataMap map) {
    this.dataMap = map;
  }

  public abstract void registerEvent(String eventName, TimeEventObserver listener);
}
