package com.lunex.timeseries;


import com.lunex.timeseries.TimeDataset.AggregateType;
import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.Double;

/**
 * Common used function to create
 */
public class DataFactory {


  public static <T extends DataElement> TimeSeriesBucket<T> createBucket(Class klass, long time, String seriesName, int bucketSize, int elementSize, AggregateType type){

    TimeSeriesBucket<T> t = new TimeSeriesBucket<T>(seriesName, bucketSize, elementSize, type, new TimeSchedule.AllDayAllWeek(elementSize));
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }


  public static TimeSeriesBucket<Double> createBucket(long time, String seriesName, int bucketSize, int elementSize, AggregateType type){

    TimeSeriesBucket<Double> t = new TimeSeriesBucket<Double>(seriesName, bucketSize, elementSize, type, new TimeSchedule.AllDayAllWeek(elementSize)){
      public Double makeElement() {
        return new Double();
      }
    };
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName, int elementSize) {

    return createSeries(klass, time, seriesName, elementSize, AggregateType.avg, -1);
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName, int elementSize,
                                                AggregateType type, int seriesSize) {

    TimeSeries<T> t = new TimeSeries<T>(seriesName, elementSize, type, seriesSize);
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeries<T> createSeries(Class klass, long time, String seriesName,
                                                                   AggregateType type, TimeSchedule schedule, int seriesSize) {

    TimeSeries<T> t = new TimeSeries<T>(seriesName, type, schedule, seriesSize);
    t.setClass(klass);
    if (time > 0) {
      t.init(time);
    }
    return t;
  }

  public static <T extends DataElement> TimeSeriesKeyMap<T> createSeriesMap(Class klass, String seriesName, int elementSize,
                                                                         AggregateType type, int seriesSize) {
    return createSeriesMap(klass, seriesName, type, new TimeSchedule.AllDayAllWeek(elementSize), seriesSize);
  }


  public static <T extends DataElement> TimeSeriesKeyMap<T> createSeriesMap(Class klass, String seriesName,
                                                                   AggregateType type, TimeSchedule schedule, int seriesSize) {

    TimeSeriesKeyMap<T> map = new TimeSeriesKeyMap<T>(seriesName, type, schedule, seriesSize);
    map.setClass(klass);
    return map;
  }

}
