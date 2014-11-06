package com.lunex.timeseries;


import com.lunex.timeseries.element.DataElement;

public interface TimeDatasetListener<T extends DataElement> {

  void onData(TimeDataset<T> data, T item);
}
