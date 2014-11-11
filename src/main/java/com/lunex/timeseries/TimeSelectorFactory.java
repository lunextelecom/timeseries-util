package com.lunex.timeseries;


import org.joda.time.DateTime;
import org.joda.time.Duration;

public class TimeSelectorFactory {

  /**
   * Construct a specific implementation of TimeSelector base on the enum
   * TimeSelector.Type
   * @param type
   * @param elementSize
   * @return
   */
  public static TimeSelector getSelector(TimeSelector.Type type, long elementSize){
      switch (type){

        case HOUR_OF_DAY:
          break;
        case HOURS_OF_DAY:
          break;
        case DAY_OF_WEEK:
          break;
        case DAYS_OF_WEEK:
          break;
        case WEEK:
          break;
        case MONTH:
          break;
        case ALL:
          break;
      }
    return new AnyTimeSelector(elementSize);
  }


  /**
   * Default time selector, allow any time to be
   */
  public static class AnyTimeSelector implements TimeSelector {
    long elementSize;

    public AnyTimeSelector(long elementSize) {
      this.elementSize = elementSize;
    }

    /**
     * Determine if the time should be as next element.
     * @return
     */
    public boolean isNext(long time, long newtime) {
      return newtime >= next(time);
    }

    public long next(long time){
      return time + elementSize;
    }

    public long prev(long time){
      return prev(time, 1);
    }

    public long prev(long time, int i) {
      return time - elementSize * i;
    }
  }

  public static class DayOfWeekSelector implements TimeSelector {

    static long day = 24*60*60*1000;
    static long week = 7*day; //days*hours*minutes*second*millis
    long elementSize;

    public DayOfWeekSelector(long elementSize) {
      this.elementSize = elementSize;
    }

    @Override
    public boolean isNext(long time, long newtime) {
      return newtime >= next(time);
    }

    @Override
    public long next(long time) {
      long next = time + elementSize;
      long today = TimeSeriesUtil.truncate(time, day);
      long nextDay = TimeSeriesUtil.truncate(next, day);
      if (today == nextDay){
        return next;
      }
      else{
        return today + week;
      }
    }

    @Override
    public long prev(long time) {
      return prev(time, 1);
    }

    @Override
    public long prev(long time, int i) {
      long prev = time - elementSize * i;
      long today = TimeSeriesUtil.truncate(time, day);
      long prevDay = TimeSeriesUtil.truncate(prev, day);
      if (today == prevDay){
        return prev;
      }
      else{
        return today - week;
      }
    }

  }
}

