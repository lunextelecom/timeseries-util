package com.lunex.timeseries;

import com.lunex.timeseries.element.Int;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CursorTest extends BaseTest{

  private final Logger log = LoggerFactory.getLogger(CursorTest.class);
  DateTime dt;
  DataEngine engine;
  TimeSchedule sch;
  TimeSeries<Int> s1;

  @BeforeClass
  public void setup() {
    log.debug("setup");

    dt = new DateTime(2014, 1, 1, 0, 0, 0);

    engine = new DataEngine(new SimpleDataProcessor());

    sch = new TimeSchedule.DayOfWeek(new int[]{dt.getDayOfWeek()}, 10 * 60 * 1000);

    s1 = DataFactory
        .createSeries(Int.class, dt.getMillis(), "myseries", TimeDataset.AggregateType.avg, sch,
                      50);
    engine.bindSeriesToEvent("neworder", s1);
  }

  @Test
  public void testCursor() {
    feedData(engine, dt, 50, sch.getElementSize());
    TimeSeriesUtil.print(System.out, s1);
    TimeSeries<Int>.Cursor<Int> cursor;
    cursor = s1.getCursor(dt.plusHours(5).getMillis());
    assert cursor.next().getTime() == dt.plusHours(5).getMillis();
    assert s1.getCursorLastOffset(0).current() == s1.last();
  }

  @Test
  public void testCursorRolling() {

  }

}

