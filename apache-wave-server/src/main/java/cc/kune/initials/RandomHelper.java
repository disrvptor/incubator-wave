/*
 *
 * Copyright (C) 2007-2013 Licensed to the Comunes Association (CA) under
 * one or more contributor license agreements (see COPYRIGHT for details).
 * The CA licenses this file to you under the GNU Affero General Public
 * License version 3, (the "License"); you may not use this file except in
 * compliance with the License. This file is part of kune.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package cc.kune.initials;

public class RandomHelper {

  private static ColorRandom random;

  private static int getFromRange(final int min, final int max, final double random) {
    return min + (int) (random * ((max - min) + 1));
  }

  public static int getInt(final int min, final int max) {
    final double random = Math.random();
    return getFromRange(min, max, random);
  }

  public static int getRandomInt(final int min, final int max) {
    if (random == null) {
      random = new ColorRandom();
      random.setSeed("", 0);
    }
    return getFromRange(min, max, random.nextDouble());
  }

  /**
   * Sets the random generator seed. We need to generate a random color for some
   * username but should be the same in the server and client side, and also
   * should be variable in the time. So we use the seed for that.
   * 
   * @param name
   *          the name
   * @param seed
   *          the seed
   */
  public static void setSeed(final String name, final int seed) {
    if (random == null) {
      random = new ColorRandom();
    }
    random.setSeed(name, seed);
  }

}
