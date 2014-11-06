package com.lunex.timeseries;

public abstract class DataProcessor implements TimeEventListener{

  private DataMap dataMap;

  public DataMap getDataMap() {
    return dataMap;
  }

  public void setDataMap(DataMap map) {
    this.dataMap = map;
  }

  public abstract void registerEvent(String eventName, TimeEventListener listener);
}
