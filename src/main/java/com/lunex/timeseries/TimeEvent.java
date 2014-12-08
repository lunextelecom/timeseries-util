package com.lunex.timeseries;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Represent an immutable event with a time element. Used for sending data to event.
 */
public class TimeEvent {

  transient static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

  private String name;
  private String key;
  private long time;
  private double value;
  private double weight;

  public TimeEvent(String name, long time, double value) {
    this(name, "default", time, value);
  }


  public TimeEvent(String name, String key, long time, double value) {
    this.name = name;
    this.key = key;
    this.time = time;
    this.value = value;
  }

  /**
   * Unique name of this event
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * If this event have need addition grouping
   */
  public String getKey() {
    return key;
  }

  /**
   * Time in epoc
   */
  public long getTime() {
    return time;
  }

  /**
   * value
   */
  public double getValue() {
    return value;
  }

  public String toString() {
    return String.format("%s %s", fmt.print(getTime()), value);
  }
}
