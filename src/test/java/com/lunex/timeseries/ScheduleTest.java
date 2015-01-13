package com.lunex.timeseries;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;


public class ScheduleTest {

  private final Logger log = LoggerFactory.getLogger(ScheduleTest.class);
  @Test
  public void testScheduleAllDay() {
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);
    Duration dur = new Duration(10*1000);
    TimeSchedule sch = new TimeSchedule.AllDayAllWeek(10 * 1000);
    assert sch.next(dt.getMillis()) == dt.plus(10*1000).getMillis();
    assert sch.next(dt.minus(dur).getMillis()) == dt.getMillis();
    assert sch.len(dt.getMillis(), dt.plusSeconds(10*60).getMillis()) == 60;
  }

  @Test
  public void testTimeZone(){
    DateTimeZone zone = DateTimeZone.forID("EST");
    log.info("{}",zone);

    DateTimeZone defaultTz = DateTimeZone.getDefault();
    log.info("{}", defaultTz);

    DateTimeZone.setDefault(DateTimeZone.forID("UTC"));
    assert defaultTz != DateTimeZone.getDefault();
    log.info("{}",DateTimeZone.getDefault());
    DateTimeZone.setDefault(defaultTz);
    assert defaultTz == DateTimeZone.getDefault();
  }

  @Test
  public void testScheduleDayOfWeek(){
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);

    TimeSchedule sch = new TimeSchedule.DayOfWeek(new int[] {dt.getDayOfWeek()}, 60* 60 * 1000);
    long next = dt.getMillis();

    for (int i = 1; i < 24; i++) {
      next = sch.next(next);
      assertPrint(next, dt.plusHours(i).getMillis());
//      assert next == dt.plusHours(i).getMillis();
    }
    next = sch.next(next);
    //should be 1 week
    assertPrint(next,dt.plusDays(7).getMillis());


    long prev = dt.getMillis();
    for (int i = 1; i < 24; i++) {
      log.info("loop {}", i);
      prev = sch.prev(prev);
      assertPrint(prev, dt.minusDays(6).minusHours(i).getMillis());
//      assert prev == dt.minusHours(i).getMillis();
    }
    prev = sch.prev(prev);
    //should be 1 week
    assertPrint(prev,dt.minusDays(7).getMillis());

    assertPrint(sch.prev(dt.getMillis(), 24),dt.minusDays(7).getMillis());
    assertPrint(sch.prev(dt.getMillis(), 25), dt.minusDays(13).minusHours(1).getMillis());
  }

  @Test
  public void testScheduleDayLength(){
    DateTime dt = new DateTime(2014, 1, 1, 0, 0, 0);

    TimeSchedule sch = new TimeSchedule.DayOfWeek(new int[] {dt.getDayOfWeek(), dt.getDayOfWeek() + 1}, 60* 60 * 1000);
    long next = dt.getMillis();
    for (int i = 1; i < 48; i++) {
      next = sch.next(next);
      assertPrint(next, dt.plusHours(i).getMillis());
//      assert next == dt.plusHours(i).getMillis();
    }
    next = sch.next(next);
    //should be 1 week
    assertPrint(next,dt.plusDays(7).getMillis());


    log.info("{}",sch.len(dt.getMillis(), dt.plusHours(3).getMillis()));
    assert sch.len(dt.getMillis(), dt.plusHours(3).getMillis()) == 3;
    log.info("{}", sch.len(dt.getMillis(), dt.plusHours(50).getMillis()));
    assert sch.len(dt.getMillis(), dt.plusHours(50).getMillis()) == 24 + 24;


  }


  private void assertPrint(long t1, long t2){

    log.info("day {} {}", new DateTime(t1).dayOfWeek().get(), new DateTime(t2).dayOfWeek().get());
    log.info("str {} {}", TimeSeriesUtil.timetoStr(t1), TimeSeriesUtil.timetoStr(t2));
    assert t1 == t2;
  }

}
