package com.lunex.timeseries;


import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.Int;

import org.joda.time.DateTime;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.List;


/**
 * Benchmark test.  Should remove loop in the test
 *
 */
@State(Scope.Thread)
public class TimeSeriesBenchmark {


  public static <T extends DataElement> TimeSeries<T> createIntSeriesNoRefl(String key,
                                                                          int bucketSize) {
    TimeSeries<Int> t = new IntTimeSeriesNoRefl(key, bucketSize);
    return (TimeSeries<T>) t;
  }

  //subclass
  public static <T extends DataElement> TimeSeries<T> createIntSeries(String key, int bucketSize) {
    TimeSeries<Int> t = new IntTimeSeries(key, bucketSize);
    return (TimeSeries<T>) t;
  }

  //anonymous
  public static <T extends DataElement> TimeSeries<T> createIntSeriesAnon(String key,
                                                                        int bucketSize) {
    TimeSeries<Int> t = new TimeSeries<Int>(key, bucketSize) {
    };
    return (TimeSeries<T>) t;
  }

  //anonymous no reflection
  public static <T extends DataElement> TimeSeries<T> createIntSeriesAnonNoRefl(String key,
                                                                        int bucketSize) {
    TimeSeries<Int> t = new TimeSeries<Int>(key, bucketSize) {
      @Override
      protected Int makeElement() {
        return new Int();
      }
    };
    return (TimeSeries<T>) t;
  }




  void populateSeries(TimeSeries<Int> series) {
    series.initTime(dt.getMillis());
    for(TimeEvent evt : events) {
      series.onEvent(evt.getTime(), evt);
    }
  }

  List<TimeEvent> events;
  DateTime dt;

  @Setup
  public void setupData(){
    events = new ArrayList<TimeEvent>();
    dt = new DateTime(2014, 1, 1, 0, 0, 0);
    for (int i = 0; i < 10000; i++) {
      TimeEvent evt = new TimeEvent("", dt.getMillis() + i * 15000, i);
      events.add(evt);
    }
  }

  @Benchmark
  public void noReflSeries(){
    TimeSeries<Int> s1 = createIntSeriesNoRefl("myseries", 30000);
    populateSeries(s1);
  }

  @Benchmark
  public void subclassSeries() {
    TimeSeries<Int> s1 = createIntSeries("myseries", 30000);
    populateSeries(s1);
  }

  @Benchmark
  public void anonymousSeries() {
    TimeSeries<Int> s1 = createIntSeriesAnon("myseries", 30000);
    populateSeries(s1);
  }

  @Benchmark
  public void anonymousNoReflSeries() {
    TimeSeries<Int> s1 = createIntSeriesAnonNoRefl("myseries", 30000);
    populateSeries(s1);
  }


  static class IntTimeSeries extends TimeSeries<Int> {

    public IntTimeSeries(String key, int bucketSize) {
      super(key, bucketSize);
    }
  }

  static class IntTimeSeriesNoRefl extends TimeSeries<Int> {

    public IntTimeSeriesNoRefl(String key, int bucketSize) {
      super(key, bucketSize);
    }

    @Override
    protected Int makeElement() {
      return new Int();
    }
  }

}
