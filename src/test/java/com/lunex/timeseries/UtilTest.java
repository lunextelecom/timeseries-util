package com.lunex.timeseries;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class UtilTest {

  private final Logger log = LoggerFactory.getLogger(UtilTest.class);

  @Test
  public void TruncateTimeTest() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);

    assert TimeSeriesUtil.truncate(dt.getMillis(), 10 * 1000) == dt.getMillis();
    assert TimeSeriesUtil.truncate(dt.getMillis() + 9999, 10 * 1000) == dt.getMillis();
    assert TimeSeriesUtil.truncate(dt.getMillis() + 10000, 10 * 1000) == dt.getMillis() + 10 * 1000;
    assert TimeSeriesUtil.truncate(dt.getMillis() + 25000, 10 * 1000) == dt.getMillis() + 20 * 1000;
    long prev = TimeSeriesUtil.truncate(dt.getMillis() - 60*1000, 24 * 60 * 60 * 1000, DateTimeZone.forID("EST"));
    log.info("{} == {}", TimeSeriesUtil.timetoStr(prev), TimeSeriesUtil.timetoStr(dt.minusDays(1).getMillis()));
    log.info("{} == {}", prev, dt.minusDays(1).getMillis());
    assert prev == dt.minusDays(1).getMillis();
  }

  @Test
  public void RelativeTimeTest() {
    DateTime dt = new DateTime(2014, 1, 1, 9, 30, 30);
    long relativeTime = TimeSeriesUtil.getRelativeTime(dt.getMillis());
    log.info("origTime     {}", dt.getMillis());
    log.info("relativeTime {}", relativeTime);
    log.info("millisofDay  {}", dt.millisOfDay().get());
    log.info("truncateDay  {}", TimeSeriesUtil.timetoStr(dt.getMillis() - dt.millisOfDay().get()));

    log.info(TimeSeriesUtil.timetoStr(relativeTime));

  }
}
