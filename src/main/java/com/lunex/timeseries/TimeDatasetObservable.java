package com.lunex.timeseries;

import java.util.List;

public interface TimeDatasetObservable {
  void register(TimeDatasetObserver observer);

  List<TimeDatasetObserver> getObservers();
}
