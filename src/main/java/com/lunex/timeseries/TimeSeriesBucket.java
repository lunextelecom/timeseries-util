package com.lunex.timeseries;

import com.lunex.timeseries.element.DataElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;


/**
 * rolling version of
 * Use this if to implement something like last 5 hours with 10 sec update
 *
 *           [      60min            ] #bucket
 * [element1][element2]...[element361][current] #series
 *
 * Notice element1 is kept so we can access the first item is the removed item.
 */
public class TimeSeriesBucket<T extends DataElement> extends TimeSeriesBase<T>
    implements TimeDataset<T> {

  private final Logger log = LoggerFactory.getLogger(TimeSeriesBucket.class);

  private int bucketSize;
  private T bucket;

  TimeSeries<T> series;
  private int seriesSize;


  public TimeSeriesBucket(String key, int bucketSize, int elementSize, AggregateType type, TimeSchedule schedule) {
    log.debug("TimeSeriesBucket {} created", key);
    this.key = key;
    this.bucketSize = bucketSize;
    this.elementSize = elementSize;
    this.type = type;
    if (bucketSize % elementSize != 0) {
      throw new RuntimeException("Bucket size must be divible of elementsize");
    }
    //added one so the first element is the
    this.seriesSize = bucketSize / elementSize;
    this.series =
        new TimeSeries<T>(key + "." + "fine", elementSize, type, schedule, seriesSize + 2){
          @Override
          protected T makeElement() {
            return TimeSeriesBucket.this.makeElement();
          }
        };
  }

  /**
   * Unique name to identify this series
   */
  @Override
  public void init(long time) {
    bucket = makeElement();
    bucket.setTime(time);
    series.init(time);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public T getData(long time) {
    return bucket;
  }

  @Override
  public T first() {
    return bucket;
  }

  @Override
  public T current() {
    return bucket;
  }

  @Override
  public T last() {
    return bucket;
  }

  /**
   * @param time  - the time generate by the sender, it might not necessary be the time of the
   *              event
   * @param event - the time Event object
   */
  @Override
  public boolean onEvent(long time, TimeEvent event) {
    if (series.onEvent(time, event)) {
      //new element is added and element might be removed.
      long startTime = Math.max(truncate(time)-bucketSize, bucket.getTime());
      boolean shift = false;
      Iterator<T> iter = series.iterator();
      while(iter.hasNext()){
        T element = iter.next();
        if (element.getTime() < startTime){
          bucket.subtract(element, getAggregateType());
          iter.remove();
          shift = true;
        }
        else{
          break;
        }
      }
      bucket.setTime(startTime);
      bucket.add(series.last(), getAggregateType());
      //set to the new time to the 2nd item

      if (shift)
        log.debug("{}.shift {}", key, bucket);
      else
        log.debug("{}.add   {}", key, bucket);
    }


    return false;
  }


  public boolean onEventFixsize(long time, TimeEvent event) {
    if (series.onEvent(time, event)) {
      //new element is added and element might be removed.
      if (series.size() >= seriesSize + 2) {
        //need to get first element
        //get last element
        bucket.subtract(series.first(), getAggregateType());
        bucket.add(series.last(), getAggregateType());
        //set to the new time to the 2nd item
        bucket.setTime(truncate(event.getTime())-bucketSize+elementSize);
        log.debug("{}.shift {}", key, bucket);
      } else {

        bucket.add(series.last(), getAggregateType());
        log.debug("{}.add   {}", key, bucket);
      }

    }
    return false;
  }

  public int getSeriesSize() {
    return seriesSize;
  }


}
