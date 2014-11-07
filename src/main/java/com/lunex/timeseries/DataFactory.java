package com.lunex.timeseries;


import com.lunex.timeseries.element.Int;

/**
 * Common used function to create
 */
public class DataFactory {




  public static TimeSeries<Int> createIntSeries(long time, String seriesName, int elementSize,
                                                TimeDataset.AggregateType type, int seriesSize) {

    TimeSeries<Int> t = new TimeSeries<Int>(seriesName, elementSize, type, seriesSize) {
      protected Int makeElement() {
        return new Int();
      }
    };
    if (time > 0) {
      t.init(time);
    }
    return t;
  }


}
