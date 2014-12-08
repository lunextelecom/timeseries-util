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
            .createSeries(Int.class, dt.getMillis(), "myseries", 5000);
    feedData(s1, dt, 20);
    TimeSeriesUtil.print(System.out, s1);
    assert s1.first().value() == 10;
    assert s1.last().value() == 60;
    assert s1.current().value() == 85;

  }

  @Test
  public void testCreateSeriesKlass() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int>
        s2 =
        DataFactory.createSeries(Int.class, dt.getMillis(), "myseries", 5000);


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
    TimeSeries<Int> s1 = DataFactory.createSeries(Int.class, dt.getMillis(), "myseries@5", 5 * 1000);
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
            .createSeries(Int.class, dt.getMillis(), "myseries", 5 * 1000, TimeDataset.AggregateType.avg,
                             5);
    engine.bindSeriesToEvent("neworder", s1);

    //create output
    engine.addDatasetListener(s1, new ConsoleSubscriber());

    feedData(engine, dt, 100);
    TimeSeriesUtil.print(System.out, s1);
    assert s1.size() == 6;
  }

  @Test
  public void testTimesSeriesDayOfWeek(){
    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSchedule sch = new TimeSchedule.DayOfWeek(new int[] {dt.getDayOfWeek()}, 60* 60 * 1000);
    TimeSeries<Int>
        s1 =
        DataFactory
            .createSeries(Int.class, dt.getMillis(), "myseries", TimeDataset.AggregateType.avg, sch,
                             50);
    engine.bindSeriesToEvent("neworder", s1);

    //create output
    engine.addDatasetListener(s1, new ConsoleSubscriber());

    feedData(engine, dt, 24*15*6, 10*60*1000);
    TimeSeriesUtil.print(System.out, s1);
//    assert s1.size() == 11;
  }

  @Test
  public void testPhantomAllDay(){
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    DataEngine engine = new DataEngine(new SimpleDataProcessor());
    TimeSchedule sch = new TimeSchedule.DayOfWeek(new int[] {dt.getDayOfWeek()}, 10* 60 * 1000);
    TimeSeries<Int>
        s1 =
        DataFactory
            .createSeries(Int.class, dt.getMillis(), "myseries", TimeDataset.AggregateType.avg, sch,
                          50);
    engine.bindSeriesToEvent("neworder", s1);

    TimeEvent evt = new TimeEvent("neworder", dt.plusMinutes(5).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    evt = new TimeEvent("neworder", dt.plusMinutes(30).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    evt = new TimeEvent("neworder", dt.plusMinutes(32).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    TimeSeriesUtil.print(System.out, s1);
    assert s1.size() == 4;

  }

  @Test
  public void testPhantomDayOfWeek(){
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    DataEngine engine = new DataEngine(new SimpleDataProcessor());
    TimeSchedule sch = new TimeSchedule.DayOfWeek(new int[] {dt.getDayOfWeek()}, 60* 60 * 1000);
    TimeSeries<Int>
        s1 =
        DataFactory
            .createSeries(Int.class, dt.getMillis(), "myseries", TimeDataset.AggregateType.avg, sch,
                          50);
    engine.bindSeriesToEvent("neworder", s1);

    TimeEvent evt = new TimeEvent("neworder", dt.plusMinutes(5).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    evt = new TimeEvent("neworder", dt.plusMinutes(129).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    evt = new TimeEvent("neworder", dt.plusDays(7).plusMinutes(105).getMillis(), 5);
    engine.onEvent(evt.getTime(), evt);
    TimeSeriesUtil.print(System.out, s1);
    assert s1.first().getTime() == dt.getMillis();
    assert s1.getElement(dt.getMillis()).value() == 5;
    assert s1.getElement(dt.plusHours(2).getMillis()).value() == 5;
    assert s1.getElement(dt.plusDays(7).plusHours(1).getMillis()).value() == 5;
  }





}