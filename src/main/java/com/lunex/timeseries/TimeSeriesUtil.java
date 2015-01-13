package com.lunex.timeseries;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.OutputStream;
import java.io.PrintWriter;

public class TimeSeriesUtil {

  static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
  static int hour = 60*60*1000;

  public static String timetoStr(long time) {
    return fmt.print(time);
  }

  /**
   * cannot truncate with duration > 1 hours as timezone will make it truncate wrong value.
   * the truncate will work according to UTC, so if the time was pass in as other time zone
   * it would be error.
   * @param time
   * @param duration
   * @return
   */
  public static long truncate(long time, long duration) {
    if (duration <= hour) {
      return (time / duration) * duration;
    } else {
      throw new RuntimeException("Truncate cannot support mroe than 1 hour");
    }
  }

  public static long truncate(long time, long duration, DateTimeZone tz){
    long offset = tz.getOffset(time);
    return -offset + ((time + offset)/ duration) * duration;
  }

  public static DateTimeZone getDefaultTimeZone(){
    return DateTimeZone.getDefault();
  }

  public static void setDefaultTimeZone(DateTimeZone tz){
    DateTimeZone.setDefault(tz);
  }

  public static long getRelativeTime(long time) {
    DateTime dt = new DateTime(time);
    MutableDateTime mtd = new MutableDateTime();
    mtd.setHourOfDay(dt.getHourOfDay());
    mtd.setMinuteOfHour(dt.getMinuteOfHour());
    mtd.setSecondOfMinute(dt.getSecondOfMinute());
    mtd.setMillisOfSecond(dt.getMillisOfSecond());
    return mtd.getMillis();
  }

  public static void print(OutputStream os, TimeDataset data) {
    PrintWriter p = new PrintWriter(os);
    p.write(data.getKey() + "@" + data.getElementSize());
    int count = 1;
    if (data instanceof Iterable) {
      p.write("\n");

      for (Object t : ((TimeSeries) data)) {
        p.write(t.toString() +":" +count++ + "\n");

      }
    } else {
      p.write("\n");
      p.write(data.current().toString());
    }
    p.flush();
  }

  public static void print(OutputStream os, TimeSeriesKeyMap data) {
    for(Object series: data.seriesMap.values()){
      print(os, (TimeDataset) series);
    }
  }

}
