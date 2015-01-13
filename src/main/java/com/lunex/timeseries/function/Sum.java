package com.lunex.timeseries.function;

public class Sum implements OneToOne {

  public Sum(int blockSize) {
    this.blockSize = blockSize;
    this.block = new double[blockSize];
  }

  private int blockSize;
  private double total = 0;
  private int removeIndex = 0;
  private double[] block;

  public double compute(double input) {
    total -= block[removeIndex];
    total += input;
    block[removeIndex] = input;
    removeIndex = (removeIndex + 1) % blockSize;
    return total;
  }

  public double value() {
    return total;
  }}
