package com.lunex.timeseries;

public interface TimeEventObserver {


  /**
   * Name of this Observer
   * @return
   */
  String getKey();

  /**
   *  @param time - the time generate by the sender, it might not necessary be the time of the event
   * @param event - the time Event object
   */
  int onEvent(long time, TimeEvent event);

}
