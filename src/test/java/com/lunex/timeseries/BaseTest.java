package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
  private final Logger log = LoggerFactory.getLogger(TimeSeriesTest.class);
  class ConsoleSubscriber implements TimeDatasetListener {

    @Override
    public void onData(TimeDataset data, DataElement item) {
      log.info(item.toString());
    }

  }

  public void feedData(TimeEventListener engine, DateTime startTime, int num){
    log.info("begin feeding data...");
    for (int i = 0; i < num; i++) {
      TimeEvent evt = new TimeEvent("neworder", startTime.getMillis() + i * 1000, i);
      engine.onEvent(evt.getTime(), evt);
    }
    log.info("finish feeding data");
  }

}
