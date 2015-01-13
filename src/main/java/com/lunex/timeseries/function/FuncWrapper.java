package com.lunex.timeseries.function;

import com.lunex.timeseries.TimeDataset;
import com.lunex.timeseries.TimeDatasetObserver;
import com.lunex.timeseries.TimeSeries;
import com.lunex.timeseries.element.DataElement;
import com.lunex.timeseries.element.Double;

public class FuncWrapper<T extends DataElement> implements TimeDatasetObserver<T> {

  TimeSeries<Double> out;
  OneToOne func;

  public FuncWrapper(OneToOne func, TimeSeries<Double> out) {
    this.func = func;
    this.out = out;
  }

  @Override
  public void onData(TimeDataset data, DataElement item, int num) {
    if (num == 1) {
      computeElement(item);
    } else if (num > 1) {
      TimeSeries.Cursor c = ((TimeSeries) data).getCursorLastOffset(-1 * (num - 1));
      for (Object obj : c) {
        computeElement((DataElement) obj);
      }
    }
  }

  private void computeElement(DataElement item) {
    double d = item.castDouble();
    double ret = func.compute(d);
    out.addElement(new Double(item.getTime(), ret));
  }

}
