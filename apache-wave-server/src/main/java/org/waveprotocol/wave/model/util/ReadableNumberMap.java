/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.waveprotocol.wave.model.util;

/**
 * A read-only interface to a map of doubles to V.
 *
 * We define this in favor of using a java.util collections interface
 * so that we can write an optimized implementation for GWT.
 *
 * Null is not permitted as a key.
 *
 * Implementations must distinguish between null values and unset keys.
 *
 * @author ohler@google.com (Christian Ohler)
 *
 * @param <V> type of values in the map
 */
public interface ReadableNumberMap<V> {
  // Maybe add a primitive hasEntry(key, value) that returns true if an
  // entry for the key exists AND the value is equal to value.

  /**
   * A procedure that accepts a key and the corresponding value from the map.
   */
  public interface ProcV<V> {
    public void apply(double key, V value);
  }

  /**
   * Return the value associated with key.  Must only be called if there
   * is one.
   */
  V getExisting(double key);

  /**
   * Return the value associated with key, or defaultValue if there is none.
   */
  V get(double key, V defaultValue);

  /**
   * Return the value associated with key, or null if there is none
   */
  V get(double key);

  /**
   * Return true iff this map contains a value for the key key.
   */
  boolean containsKey(double key);

  /**
   * Return true iff this map does not contain a value for any key.
   */
  boolean isEmpty();

  /**
   * Call the callback for every key-value pair in the map, in undefined
   * order.
   */
  void each(ProcV<V> callback);

  /**
   * Count the number of key-value pairs in the map.
   *
   * Note: Depending on the underlying map implementation, this may be a
   * time-consuming operation.
   */
  int countEntries();

}
