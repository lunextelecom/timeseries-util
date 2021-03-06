package com.lunex.timeseries;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class TimeSchedule {

  protected final Logger log = LoggerFactory.getLogger(TimeSchedule.class);

  protected int elementSize;



  public abstract long next(long time);

  public abstract boolean isValid(long time);

  public long prev() {
    return prev(1);
  }

  public long prev(long time) {
    return prev(time, 1);
  }


  public abstract long prev(long time, int i);

  public int getElementSize() {
    return elementSize;
  }

  public abstract int len(long start, long end);

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

    @Override
    public int len(long start, long end) {
      return (int)((end - start)/elementSize);
    }
  }

  /**
   * to build a schedule so so it is always the same day. input time must be truncated.
   */
  public static class DayOfWeek extends TimeSchedule {

    static long day = 24 * 60 * 60 * 1000;
    static long week = 7 * day; //days*hours*minutes*second*millis

    Set daysOfWeek;
    Map<Integer, Integer> dayGap;
    DateTimeZone tz;

    public DayOfWeek(int[] dayOfWeek, int elementSize) {
      this(dayOfWeek, elementSize, DateTimeZone.getDefault());
    }

    public DayOfWeek(int[] dayOfWeek, int elementSize, DateTimeZone tz) {
      this.daysOfWeek = Sets.newHashSet(Ints.asList(dayOfWeek));
      log.info("daysOfWeek {}", daysOfWeek);
      this.elementSize = elementSize;
      this.tz = tz;
      dayGap = new HashMap();
      for (int day = 1; day <= 7; day++) {
        int count = 1;
        for (int gap = 1; gap <= 7; gap++) {
          int nextValidDay = (day + gap)%7;
          if (!daysOfWeek.contains(nextValidDay)){
            count++;
          }
          else{
            break;
          }
        }
        dayGap.put(day, count);
      }
      log.info("dayGap {}", dayGap);
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
        return today + (dayGap.get(new DateTime(today).getDayOfWeek()))*day;

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
        return today - dayGap.get(new DateTime(today).getDayOfWeek())*day + day - elementSize;
      }
    }

    @Override
    public long prev(long time, int i) {
      for (int j = 0; j < i; j++) {
        time = prev(time);
      }
      return time;
    }

    /**
     * compute the element between start and end.
     * @param start
     * @param end
     * @return
     */
    @Override
    public int len(long start, long end) {
      int ret = 0;
      long time = start;
      while(time < end){
        time = this.next(time);
        ret++;
      }

      return ret;
    }
  }




}
