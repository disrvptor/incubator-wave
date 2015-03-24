package com.google.wave.api.data;

import com.google.wave.api.Range;

/**
   * Singleshot returns a single range.
   */
  public class Singleshot implements DocumentHitIterator {

    private final Range range;
    private boolean called;

    public Singleshot(Range range) {
      this.called = false;
      this.range = range;
    }

    @Override
    public Range next() {
      if (called) {
        return null;
      }
      called = true;
      return this.range;
    }

    @Override
    public void shift(int where, int delta) {
    }
  }