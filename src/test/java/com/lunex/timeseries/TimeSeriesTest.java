package com.lunex.timeseries;



import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.Int;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TimeSeriesTest {
  private final Logger log = LoggerFactory.getLogger(TimeSeriesTest.class);

  @Test
  public void testIntItem() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    Int item = new Int(dt.getMillis(), 1);
    System.out.println(item);
  }


  @Test
  public void testCreateSeries() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int> s1 = DataFactory.createIntSeries(dt.getMillis(), "myseries", 5000);
    for (int i = 0; i < 20; i++) {
      TimeEvent evt = new TimeEvent("", dt.getMillis() + i * 1000, i);
      s1.onEvent(evt.getTime(), evt);
    }
    TimeSeriesUtil.print(System.out, s1);
  }

  @Test
  public void testDataEngine() {
    log.info("testDataEngine");
    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeries<Int> s1 = DataFactory.createIntSeries(dt.getMillis(), "myseries@5", 5 * 1000);
//    TimeSeries<Int> s2 = DataFactory.createIntSeries(dt.getMillis(), "myseries@30", 30 * 1000);
    engine.bindSeriesToEvent("neworder", s1);
//    engine.bindSeriesToEvent("neworder", s2);

    //create output
    engine.addDatasetListener(s1, new ConsoleSubscriber());
//    engine.addDatasetListener(s2, new ConsoleSubscriber());

    for (int i = 0; i < 20; i++) {
      TimeEvent evt = new TimeEvent("neworder", dt.getMillis() + i * 1000, i);
      engine.onEvent(evt.getTime(), evt);
    }

    TimeSeriesUtil.print(System.out, s1);
  }

  class ConsoleSubscriber implements TimeDatasetListener {

    @Override
    public void onData(TimeDataset data, DataElement item) {
      log.info(item.toString());
    }

  }

}