package com.lunex.timeseries;


import com.lunex.timeseries.element.Int;

/**
 * Common used function to create
 */
public class DataFactory {


  public static TimeSeries<Int> createIntSeries(long time, String seriesName, int bucketSize) {
    return createIntSeries(time, seriesName, bucketSize, TimeDataset.AggregateType.avg);
  }


  public static TimeSeries<Int> createIntSeries(long time, String seriesName, int bucketSize,
                                                TimeDataset.AggregateType type) {

    TimeSeries<Int> t = new TimeSeries<Int>(seriesName, bucketSize, type) {
      protected Int makeElement() {
        return new Int();
      }
    };
    if (time > 0) {
      t.initTime(time);
    }
    return t;
  }


}
