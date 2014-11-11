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
   * Add and subtract bucket from orig. add, subtract must be of same size.  size of add, subtract must be
   * divisor by orig.size
   */
  public static <T extends DataElement> void shift(T orig, T add, T remove,
                                                 TimeDataset.AggregateType type) {

  }

  public static <T extends DataElement> void add(T bucket, T add, TimeDataset.AggregateType type) {
  }


  public static String timetoStr(long time){
    return fmt.print(time);
  }

  public static long truncate(long time, long duration) {
    return (time / duration) * duration;
  }

  public static void print(OutputStream os, TimeDataset data) {


    PrintWriter p = new PrintWriter(os);
    p.write(data.getKey() +"@" + data.getElementSize());
    if (data instanceof Iterable) {
      p.write("series\n");
      for (Object t : ((TimeSeries) data)) {
        p.write(t.toString() + "\n");

      }
    }
    else {
      p.write("bucket\n");
      p.write(data.current().toString());
    }
    p.flush();
  }
}
