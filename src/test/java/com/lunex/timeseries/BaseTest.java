package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
  private final Logger log = LoggerFactory.getLogger(TimeSeriesTest.class);
  class ConsoleSubscriber implements TimeDatasetObserver {

    @Override
    public void onData(TimeDataset data, DataElement item, int num) {
      log.info(item.toString());
    }

  }

  public void feedData(TimeEventObserver engine, DateTime startTime, int num){
    feedData(engine, startTime, num, 1000);
  }

  public void feedData(TimeEventObserver engine, DateTime startTime, int num, int incre){
    feedData(engine, startTime, num, incre, new String[] {"sellerA"});
  }

  public void feedData(TimeEventObserver engine, DateTime startTime, int num, int incre, String[] keys){
    log.info("begin feeding data...");
    for (int i = 0; i < num; i++) {
      for (String key: keys){
        TimeEvent evt = new TimeEvent("neworder", key, startTime.getMillis() + i * incre, i);
        engine.onEvent(evt.getTime(), evt);
      }
    }
    log.info("finish feeding data");
  }


}
