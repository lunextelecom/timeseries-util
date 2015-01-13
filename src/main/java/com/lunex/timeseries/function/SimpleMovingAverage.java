package com.lunex.timeseries.function;

public class SimpleMovingAverage implements OneToOne {

  public SimpleMovingAverage(int blockSize) {
    this.blockSize = blockSize;
    this.block = new double[blockSize];
  }

  private double sma = 0;
  private int blockSize;
  private double total = 0;
  private int counter = 0;
  private int removeIndex = 0;
  private double[] block;

  public double compute(double input) {
    total -= block[removeIndex];
    total += input;
    block[removeIndex] = input;
    removeIndex = (removeIndex + 1) % blockSize;
    counter++;
    if (counter < blockSize) {
      sma = total / counter;
    } else {
      sma = total / blockSize;
    }
    return sma;
  }

  public double value() {
    return sma;
  }
}
