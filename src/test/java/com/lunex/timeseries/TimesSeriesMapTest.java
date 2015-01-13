package com.lunex.timeseries;

import com.lunex.timeseries.TimeDataset.AggregateType;
import com.lunex.timeseries.element.Int;
import com.lunex.timeseries.function.SimpleMovingAverage;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TimesSeriesMapTest extends BaseTest {
  private final Logger log = LoggerFactory.getLogger(TimeSeriesTest.class);


  @Test
  public void testMap(){
    //avg order of day per seller

    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    TimeSeriesKeyMap<Int>
        sm =
        DataFactory
            .createSeriesMap(Int.class, "myseries", 5 * 1000, AggregateType.avg,
                             10);

    engine.bindSeriesToEvent("neworder", sm);

    //create output
    //the series of sm should proxed to sm

    engine.addDatasetListener(sm, new ConsoleSubscriber());

    //alert("myseries.avgorder", > 1)
    engine.applyFunc(new SimpleMovingAverage(30), sm, "avgorder");
    feedData(engine, dt, 100, 1000, new String[]{"A", "B", "C"});
    TimeSeriesUtil.print(System.out, sm);

//    TimeSeriesUtil.print(System.out, engine.getDataset(sm.getKey() + ".avgorder"));
  }
}
