package com.lunex.timeseries;

import com.lunex.timeseries.element.Double;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TimeSeriesBucketTest extends BaseTest {
  private final Logger log = LoggerFactory.getLogger(TimeSeriesBucketTest.class);


  @Test
  public void TestBucket()
  {
    DataEngine engine = new DataEngine(new SimpleDataProcessor());

    //create series
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    int bucksetSize = 60*1000;
    int elementSize = 5*1000;

//    TimeSeriesBucket<Double> bucket = DataFactory.createBucket(dt.getMillis(), "salelast30", 60 * 1000, 5000,
//                                                     TimeDataset.AggregateType.avg);
    TimeSeriesBucket<Double> bucket = DataFactory.createBucket(Double.class, dt.getMillis(), "salelast30", 60 * 1000, 5000,
                                                               TimeDataset.AggregateType.avg);

    engine.bindSeriesToEvent("neworder", bucket);
    feedData(engine, dt, 100);
    TimeSeriesUtil.print(System.out, bucket.series);
    TimeSeriesUtil.print(System.out, bucket);
    double total = 0;
    int weight = 0;
    int count = 0;
    log.info("first   {}", bucket.series.first());
    log.info("last    {}", bucket.series.last());
    log.info("current {}", bucket.series.current());
    for(Double element: bucket.series){
      log.info("element adding {} {}", element, element == bucket.series.current());
      if (element == bucket.series.current()) break;
      count++;
      total += element.value();
      weight += element.getWeight();
    }
    log.info("Expect {} == {}", total / count, bucket.current().value());

    assert bucket.getSeriesSize() == bucksetSize/elementSize;
    assert bucket.current().getWeight() == weight;


    assert bucket.current().value() == total/bucket.getSeriesSize();
  }


}
