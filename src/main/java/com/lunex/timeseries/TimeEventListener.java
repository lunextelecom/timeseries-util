package com.lunex.timeseries;

public interface TimeEventListener {


  /**
   * Name of this Listener
   * @return
   */
  String getKey();

  /**
   *
   * @param time - the time generate by the sender, it might not necessary be the time of the event
   * @param event - the time Event object
   */
  boolean onEvent(long time, TimeEvent event);

}
