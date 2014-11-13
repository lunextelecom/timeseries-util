package com.lunex.timeseries;


import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.Int;
import com.lunex.timeseries.element.Double;

/**
 * Common used function to create
 */
public class DataFactory {


  public static TimeSeriesBucket<Double> createBucket(long time, String seriesName, int bucketSize, int elementSize, TimeDataset.AggregateType type){

    TimeSeriesBucket<Double> t = new TimeSeriesBucket<Double>(seriesName, bucketSize, elementSize, type, null){
      protected Double makeElement() {
        return new Double();
      }
    };
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static TimeSeries<Int> createIntSeries(long time, String seriesName, int elementSize,
                                                TimeDataset.AggregateType type, int seriesSize) {

//    TimeSeries<Int> t = new TimeSeries<Int>(seriesName, elementSize, type, seriesSize) {
//      protected Int makeElement() {
//        return new Int();
//      }
//    };
    TimeSeries<Int> t = new TimeSeries<Int>(seriesName, elementSize, type, seriesSize){};

    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName, int elementSize,
                                                                   TimeDataset.AggregateType type) {

    TimeSeries<T> t = new TimeSeries<T>(seriesName, elementSize, type, -1);
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName, int elementSize,
                                                TimeDataset.AggregateType type, int seriesSize) {

    TimeSeries<T> t = new TimeSeries<T>(seriesName, elementSize, type, seriesSize);
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName, int elementSize,
                                                                   TimeDataset.AggregateType type, TimeSchedule schedule, int seriesSize) {

    TimeSeries<T> t = new TimeSeries<T>(seriesName, elementSize, type, schedule, seriesSize);
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }


}
