package com.lunex.timeseries;

import com.lunex.timeseries.TimeDataset.AggregateType;
import com.lunex.timeseries.element.DataElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holder for map of time series. Series are generated by mapper.
 */
public class TimeSeriesKeyMap<T extends DataElement> extends TimeSeriesBase<T> implements
                                                                            TimeEventObserver,
                                                                                       TimeDatasetObserver, DatasetCreateObserver {

  private final Logger log = LoggerFactory.getLogger(TimeSeriesKeyMap.class);

  Map<String, TimeSeries<T>> seriesMap;
  TimeSchedule schedule;
  private int seriesSize;
  List<DatasetCreateObserver<T>> createObservers ;

  public TimeSeriesKeyMap(String key, AggregateType type, TimeSchedule schedule, int seriesSize) {
    this.seriesMap = new HashMap<String, TimeSeries<T>>();
    this.createObservers = new ArrayList<DatasetCreateObserver<T>>();
    this.key = key;
    this.elementSize = schedule.elementSize;
    this.type = type;
    this.schedule = schedule;
    this.seriesSize = seriesSize;
  }

  /**
   * @param time  - the time generate by the sender, it might not necessary be the time of the
   *              event
   * @param event - the time Event object
   */
  @Override
  public int onEvent(long time, TimeEvent event) {
    String eventKey = event.getKey();

    TimeSeries<T> series = seriesMap.get(eventKey);
    if (series == null){
      //create the series, put it into the map
      series =
          new TimeSeries<T>(key + "." + eventKey, type, schedule, seriesSize){
            @Override
            public T makeElement() {
              return TimeSeriesKeyMap.this.makeElement();
            }
          };
      seriesMap.put(eventKey, series);
      series.init(event.getTime());
      onSeriesCreate(series);
    }
    return series.onEvent(time, event);
  }

  /**
   * Proxy these events to subscriber of this map
   * @param data
   * @param item
   * @param num
   */
  @Override
  public void onData(TimeDataset data, DataElement item, int num) {
    log.info("proxy");
    for(TimeDatasetObserver sub:subscribers){
      sub.onData(data, item, num);
    }
  }

  @Override
  public void onSeriesCreate(TimeSeries series) {
    for (DatasetCreateObserver sub : createObservers) {
      sub.onSeriesCreate(series);
    }
  }


}
