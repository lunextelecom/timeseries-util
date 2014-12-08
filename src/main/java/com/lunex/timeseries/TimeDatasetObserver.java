package com.lunex.timeseries;


import com.lunex.timeseries.element.DataElement;

public interface TimeDatasetObserver<T extends DataElement> {

  void onData(TimeDataset<T> data, T item, int num);
}
