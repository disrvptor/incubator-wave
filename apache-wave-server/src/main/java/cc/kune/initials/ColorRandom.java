package cc.kune.initials;

import java.util.Date;

/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * INCLUDES MODIFICATIONS BY RICHARD ZSCHECH AS WELL AS GOOGLE.
 */

/**
 * This class provides methods that generates pseudo-random numbers of different
 * types, such as {@code int}, {@code long}, {@code double}, and {@code float}.
 * It follows the algorithms specified in the JRE javadoc.
 * 
 * This emulated version of Random is not serializable.
 */
public class ColorRandom {

  private static final double multiplierHi = 0x5de;
  private static final double multiplierLo = 0xece66d;
  private static final double twoToThe24 = 16777216.0;
  private static final double twoToThe31 = 2147483648.0;
  private static final double twoToThe32 = 4294967296.0;
  private static final double twoToTheMinus24 = 5.9604644775390625e-8;
  private static final double twoToTheMinus26 = 1.490116119384765625e-8;
  private static final double twoToTheMinus31 = 4.656612873077392578125e-10;
  private static final double twoToTheMinus53 = 1.1102230246251565404236316680908203125e-16;
  private static final double[] twoToTheXMinus24 = new double[25];
  private static final double[] twoToTheXMinus48 = new double[33];

  /**
   * A value used to avoid two random number generators produced at the same
   * time having the same seed.
   */
  private static int uniqueSeed = 0;

  // Initialize power-of-two tables
  static {
    double twoToTheXMinus48Tmp = 1.52587890625e-5; // 1.0 / (1 << (48 - 32));
    for (int i = 32; i >= 0; i--) {
      twoToTheXMinus48[i] = twoToTheXMinus48Tmp;
      twoToTheXMinus48Tmp *= 0.5;
    }

    double twoToTheXMinus24Tmp = 1.0;
    for (int i = 24; i >= 0; i--) {
      twoToTheXMinus24[i] = twoToTheXMinus24Tmp;
      twoToTheXMinus24Tmp *= 0.5;
    }
  }

  /**
   * Return the number of milliseconds since 1/1/1970 as a double in order to
   * avoid the use os LongLib.
   */
  private static native double currentTimeMillis() /*-{
		return (new Date()).getTime();
  }-*/;

  /**
   * The boolean value indicating if the second Gaussian number is available.
   */
  private boolean haveNextNextGaussian = false;

  /**
   * The second Gaussian generated number.
   */
  private double nextNextGaussian;

  /**
   * The high 24 bits of the 48=bit seed value.
   */
  private double seedhi;

  /**
   * The low 24 bits of the 48=bit seed value.
   */
  private double seedlo;

  /**
   * Construct a random generator with the current time of day in milliseconds
   * plus a unique counter value as the initial state.
   * 
   * @see #setSeed
   */
  public ColorRandom() {
    // final double seed = uniqueSeed++ + currentTimeMillis();
    // final int hi = (int) Math.floor(seed * twoToTheMinus24) & 0xffffff;
    // final int lo = (int) (seed - (hi * twoToThe24));
    // setSeed(hi, lo);
  }

  /**
   * Construct a random generator with the given {@code seed} as the initial
   * state.
   * 
   * @param seed
   *          the seed that will determine the initial state of this random
   *          number generator.
   * @see #setSeed
   */
  public ColorRandom(final long seed) {
    setSeed(seed);
  }

  /**
   * Returns a pseudo-random uniformly distributed {@code int} value of the
   * number of bits specified by the argument {@code bits} as described by
   * Donald E. Knuth in <i>The Art of Computer Programming, Volume 2:
   * Seminumerical Algorithms</i>, section 3.2.1.
   * 
   * @param bits
   *          number of bits of the returned value.
   * @return a pseudo-random generated int number.
   * @see #nextBytes
   * @see #nextDouble
   * @see #nextFloat
   * @see #nextInt()
   * @see #nextInt(int)
   * @see #nextGaussian
   * @see #nextLong
   */
  protected synchronized int next(final int bits) {
    return (int) nextInternal(bits);
  }

  /**
   * Returns the next pseudo-random, uniformly distributed {@code boolean} value
   * generated by this generator.
   * 
   * @return a pseudo-random, uniformly distributed boolean value.
   */
  public boolean nextBoolean() {
    return nextInternal(1) != 0;
  }

  /**
   * Modifies the {@code byte} array by a random sequence of {@code byte}s
   * generated by this random number generator.
   * 
   * @param buf
   *          non-null array to contain the new random {@code byte}s.
   * @see #next
   */
  public void nextBytes(final byte[] buf) {
    if (buf == null) {
      throw new NullPointerException();
    }

    int rand = 0, count = 0, loop = 0;
    while (count < buf.length) {
      if (loop == 0) {
        rand = (int) nextInternal(32);
        loop = 3;
      } else {
        loop--;
      }
      buf[count++] = (byte) rand;
      rand >>= 8;
    }
  }

  /**
   * Generates a normally distributed random {@code double} number between 0.0
   * inclusively and 1.0 exclusively.
   * 
   * @return a random {@code double} in the range [0.0 - 1.0)
   * @see #nextFloat
   */
  public double nextDouble() {
    return nextInternal(26) * twoToTheMinus26 + nextInternal(27) * twoToTheMinus53;
  }

  /**
   * Generates a normally distributed random {@code float} number between 0.0
   * inclusively and 1.0 exclusively.
   * 
   * @return float a random {@code float} number between [0.0 and 1.0)
   * @see #nextDouble
   */
  public float nextFloat() {
    return (float) (nextInternal(24) * twoToTheMinus24);
  }

  /**
   * Pseudo-randomly generates (approximately) a normally distributed
   * {@code double} value with mean 0.0 and a standard deviation value of
   * {@code 1.0} using the <i>polar method<i> of G. E. P. Box, M. E. Muller, and
   * G. Marsaglia, as described by Donald E. Knuth in <i>The Art of Computer
   * Programming, Volume 2: Seminumerical Algorithms</i>, section 3.4.1,
   * subsection C, algorithm P.
   * 
   * @return a random {@code double}
   * @see #nextDouble
   */
  public synchronized double nextGaussian() {
    if (haveNextNextGaussian) {
      // if X1 has been returned, return the second Gaussian
      haveNextNextGaussian = false;
      return nextNextGaussian;
    }

    double v1, v2, s;
    do {
      // Generates two independent random variables U1, U2
      v1 = 2 * nextDouble() - 1;
      v2 = 2 * nextDouble() - 1;
      s = v1 * v1 + v2 * v2;
    } while (s >= 1);

    // See errata for TAOCP vol. 2, 3rd ed. for proper handling of s == 0 case
    // (page 5 of http://www-cs-faculty.stanford.edu/~uno/err2.ps.gz)
    final double norm = (s == 0) ? 0.0 : Math.sqrt(-2.0 * Math.log(s) / s);
    nextNextGaussian = v2 * norm;
    haveNextNextGaussian = true;
    return v1 * norm;
  }

  /**
   * Generates a uniformly distributed 32-bit {@code int} value from the random
   * number sequence.
   * 
   * @return a uniformly distributed {@code int} value.
   * @see java.lang.Integer#MAX_VALUE
   * @see java.lang.Integer#MIN_VALUE
   * @see #next
   * @see #nextLong
   */
  public int nextInt() {
    return (int) nextInternal(32);
  }

  /**
   * Returns a new pseudo-random {@code int} value which is uniformly
   * distributed between 0 (inclusively) and the value of {@code n}
   * (exclusively).
   * 
   * @param n
   *          the exclusive upper border of the range [0 - n).
   * @return a random {@code int}.
   */
  public int nextInt(final int n) {
    if (n > 0) {
      if ((n & -n) == n) {
        return (int) ((n * nextInternal(31)) * twoToTheMinus31);
      }
      double bits, val;
      do {
        bits = nextInternal(31);
        val = bits % n;
      } while (bits - val + (n - 1) < 0);
      return (int) val;
    }

    throw new IllegalArgumentException();
  }

  private synchronized double nextInternal(final int bits) {
    double hi = seedhi * multiplierLo + seedlo * multiplierHi;
    double lo = seedlo * multiplierLo + 0xb;
    final double carry = Math.floor(lo * twoToTheMinus24);
    hi += carry;
    lo -= carry * twoToThe24;
    hi %= twoToThe24;

    seedhi = hi;
    seedlo = lo;

    if (bits <= 24) {
      // High bits of seedhi, shifted right
      return Math.floor(seedhi * twoToTheXMinus24[bits]);
    } else {
      // All bits of seedhi, shifted left
      final double h = seedhi * (1 << (bits - 24));
      // High bits of seedlo, shifted right
      final double l = Math.floor(seedlo * twoToTheXMinus48[bits]);
      // Sum of high and low bits
      double dval = h + l;
      // Force sign based on bit 31
      if (dval >= twoToThe31) {
        dval -= twoToThe32;
      }
      return dval;
    }
  }

  /**
   * Generates a uniformly distributed 64-bit integer value from the random
   * number sequence.
   * 
   * @return 64-bit random integer.
   * @see java.lang.Integer#MAX_VALUE
   * @see java.lang.Integer#MIN_VALUE
   * @see #next
   * @see #nextInt()
   * @see #nextInt(int)
   */
  public long nextLong() {
    return ((long) nextInternal(32) << 32) + (long) nextInternal(32);
  }

  // seedhi and seedlo should be integers from 0 to 2^24 - 1
  private synchronized void setSeed(final int seedhi, final int seedlo) {
    this.seedhi = seedhi ^ 0x5de;
    this.seedlo = seedlo ^ 0xece66d;
    haveNextNextGaussian = false;
  }

  /**
   * Modifies the seed a using linear congruential formula presented in <i>The
   * Art of Computer Programming, Volume 2</i>, Section 3.2.1.
   * 
   * @param seed
   *          the seed that alters the state of the random number generator.
   * @see #next
   * @see #Random()
   * @see #Random(long)
   */
  public synchronized void setSeed(final long seed) {
    setSeed((int) ((seed >> 24) & 0xffffff), (int) (seed & 0xffffff));
  }

  public void setSeed(final String name, final int iSeed) {
    // final double seed = uniqueSeed++ + currentTimeMillis();
    final Date d = new Date();
    // Calendar no emulated in GWT
    // final Date d2 = new Date(d.getYear(), d.getMonth(), d.getDay(),
    // d.getHours(), d.getMinutes());
    final Date d2 = new Date(d.getYear(), d.getMonth(), d.getDay(), d.getHours(), 0);
    final double seed = d2.getTime() + (iSeed - name.length() + (name.charAt(0))) * 1000;
    final int hi = (int) Math.floor(seed * twoToTheMinus24) & 0xffffff;
    final int lo = (int) (seed - (hi * twoToThe24));
    setSeed(hi, lo);
  }
}