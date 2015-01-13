package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

public interface DatasetCreateObserver<T extends DataElement> {

  void onSeriesCreate(TimeSeries<T> series);
}