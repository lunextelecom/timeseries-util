package com.lunex.timeseries;


import com.lunex.timeseries.element.Int;
import com.lunex.timeseries.element.Double;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TimeSeriesTest extends BaseTest {

  private final Logger log = LoggerFactory.getLogger(TimeSeriesTest.class);


  @Test
  public void testCreateSeries() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int>
        s1 =
        DataFactory
            .createIntSeries(dt.getMillis(), "myseries", 5000, TimeDataset.AggregateType.avg, -1);
    feedData(s1, dt, 20);
    assert s1.first().value() == 10;
    assert s1.last().value() == 60;
    assert s1.current().value() == 85;
    TimeSeriesUtil.print(System.out, s1);
  }

  @Test
  public void testCreateSeriesKlass() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int>
        s2 =
        DataFactory.createSeries(Int.class, dt.getMillis(), "myseries", 5000,
                                 TimeDataset.AggregateType.avg, -1);


    feedData(s2, dt, 20);
    assert s2.first().value() == 10;
    assert s2.last().value() == 60;
    assert s2.current().value() == 85;
    TimeSeriesUtil.print(System.out, s2);
  }


  @Test
  public void testDataEngine() {
    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int> s1 = DataFactory.createIntSeries(dt.getMillis(), "myseries@5", 5 * 1000,
                                                     TimeDataset.AggregateType.avg, -1);
    engine.bindSeriesToEvent("neworder", s1);

    //create output
    engine.addDatasetListener(s1, new ConsoleSubscriber());
    feedData(engine, dt, 20);
    assert s1.first().value() == 10;
    assert s1.last().value() == 60;
    assert s1.current().value() == 85;
    TimeSeriesUtil.print(System.out, s1);
  }

  @Test
  public void testRollingTimeSeries() {
    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int>
        s1 =
        DataFactory
            .createIntSeries(dt.getMillis(), "myseries", 5 * 1000, TimeDataset.AggregateType.avg,
                             5);
    engine.bindSeriesToEvent("neworder", s1);

    //create output
    engine.addDatasetListener(s1, new ConsoleSubscriber());

    feedData(engine, dt, 100);
    TimeSeriesUtil.print(System.out, s1);
    assert s1.size() == 6;


  }

}