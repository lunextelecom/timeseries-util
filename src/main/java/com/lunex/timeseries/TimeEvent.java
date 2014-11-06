package com.lunex.timeseries;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represent an immutable event with a time element.
 * Used for sending data to event.
 */
public class TimeEvent {

  transient static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

  private String key;
  private long time;
  private double value;
  private double weight;

  public TimeEvent(String key, long time, double value) {
    this.key = key;
    this.time = time;
    this.value = value;
  }

  /**
   * Unique key for this event
   * @return
   */
  public String getKey() {
    return key;
  }

  /**
   * Time in epoc
   * @return
   */
  public long getTime() {
    return time;
  }

  /**
   * value
   * @return
   */
  public double getValue() {
    return value;
  }

  public String toString() {
    return String.format("%s %s", fmt.print(getTime()), value);
  }
}
