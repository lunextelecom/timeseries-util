package com.lunex.timeseries;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class TimeSchedule {

  protected final Logger log = LoggerFactory.getLogger(TimeSchedule.class);
  protected long time;
  protected int elementSize;


  public long next() {
    return next(time);
  }

  public abstract long next(long time);

  public abstract boolean isValid(long time);

  public long prev() {
    return prev(1);
  }

  public long prev(long time) {
    return prev(time, 1);
  }

  public long prev(int i) {
    return prev(time, i);
  }

  public abstract long prev(long time, int i);

  public void setCurTime(long curTime) {
    this.time = curTime;
  }

  public int getElementSize() {
    return elementSize;
  }

  /**
   * Default time selector, include all day all week
   */
  public static class AllDayAllWeek extends TimeSchedule {


    public AllDayAllWeek(int elementSize) {
      this.elementSize = elementSize;
    }

    public long next(long time) {
      return time + elementSize;
    }

    @Override
    public boolean isValid(long time) {
      return true;
    }

    public long prev(long time, int i) {
      return time - elementSize * i;
    }
  }

  /**
   * to build a schedule so so it is always the same day. input time must be truncated.
   */
  public static class DayOfWeek extends TimeSchedule {

    static long day = 24 * 60 * 60 * 1000;
    static long week = 7 * day; //days*hours*minutes*second*millis

    Set daysOfWeek;
    DateTimeZone tz;

    public DayOfWeek(int[] dayOfWeek, int elementSize) {
      this(dayOfWeek, elementSize, DateTimeZone.getDefault());
    }

    public DayOfWeek(int[] dayOfWeek, int elementSize, DateTimeZone tz) {
      this.daysOfWeek = Sets.newHashSet(Ints.asList(dayOfWeek));
      this.elementSize = elementSize;
      this.tz = tz;
    }

    @Override
    public long next(long time) {
      long next = time + elementSize;
//      log.debug("time {} {}", TimeSeriesUtil.timetoStr(time), time);
//      log.debug("next {} {}", TimeSeriesUtil.timetoStr(next), next);
      if (daysOfWeek.contains(new DateTime(next).getDayOfWeek())){
        return next;
      }
      else{
        long today = TimeSeriesUtil.truncate(time, day, tz);
        return today + week;
      }
    }

    @Override
    public boolean isValid(long time) {
      return daysOfWeek.contains(new DateTime(time).getDayOfWeek());
    }

    @Override
    public long prev(long time) {
      long prev = time - elementSize;
//      log.debug("time {} {}", TimeSeriesUtil.timetoStr(time), time);
//      log.debug("prev {} {}", TimeSeriesUtil.timetoStr(prev), prev);
      if (daysOfWeek.contains(new DateTime(prev).getDayOfWeek())){
        return prev;
      }
      else {
        long today = TimeSeriesUtil.truncate(time, day, tz);
        return today - week + day - elementSize;
      }
    }

    @Override
    public long prev(long time, int i) {
      for (int j = 0; j < i; j++) {
        time = prev(time);
      }
      return time;
    }
  }


  //todo finish implement PartOfDay
  public static class PartOfDay extends TimeSchedule {

    long startTime;
    long numElement;
    long elementSize;

    DateTime curTime;

    /**
     * @param startTime  - start time in millis with 0 = 00:00:00
     * @param numElement - the duration in millis to include since startTime
     */
    public PartOfDay(long startTime, long numElement, long elementSize) {
      this.startTime = TimeSeriesUtil.truncate(startTime, elementSize);
      this.numElement = numElement;
      this.elementSize = elementSize;
    }

    @Override
    public void setCurTime(long time) {
      super.setCurTime(time);
      curTime = new DateTime(time);
    }

    @Override
    public long next() {
      DateTime newTime = curTime.plus(elementSize);
      int x = newTime.millisOfDay().get();
      return 0;
    }

    @Override
    public long next(long time) {
      return 0;
    }

    @Override
    public boolean isValid(long time) {
      return false;
    }

    @Override
    public long prev(long time, int i) {
      return 0;
    }

  }


}
