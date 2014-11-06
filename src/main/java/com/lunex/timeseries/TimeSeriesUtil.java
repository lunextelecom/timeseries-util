package com.lunex.timeseries;



import com.lunex.timeseries.element.DataElement;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.OutputStream;
import java.io.PrintWriter;

public class TimeSeriesUtil<T extends DataElement> {

  static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss");

  /**
   * Filled time gap for the series
   */
//  public static <T extends AbstractItem> void FillTimeGap(TimeSeries<T> series) {
//
//  }

  /**
   * Add and remove item from orig. add, remove must be of same size.  size of add, remove must be
   * divisor by orig.size
   */
  public static <T extends DataElement> void shift(T orig, T add, T remove,
                                                 TimeDataset.AggregateType type) {

  }

  public static <T extends DataElement> void addEvent(TimeDataset<T> data, TimeEvent event) {

  }

  public static void print(OutputStream os, TimeDataset data) {


    PrintWriter p = new PrintWriter(os);
    p.write(data.getKey() +"@" + data.getBucketSize());
    if (data instanceof Iterable) {
      p.write("series\n");
      for (Object t : ((TimeSeries) data)) {
        p.write(t.toString() + "\n");

      }
    } else {
      p.write("bucket\n");
      p.write(data.toString());
    }
    p.close();
  }
}
