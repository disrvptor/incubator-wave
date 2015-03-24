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

package com.google.wave.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.wave.api.Function.BlipContentFunction;
import com.google.wave.api.Function.MapFunction;

/**
 * A class that represents a set of references to contents in a blip.
 *
 * A {@link BlipContentRefs} instance for example can represent the
 * results of a search, an explicitly set range, a regular expression, or refer
 * to the entire blip.
 *
 * {@link BlipContentRefs} are used to express operations on a blip in a
 * consistent way that can be easily transfered to the server.
 */
public interface BlipContentRefs {

	/**
	 * Inserts the given arguments at the matched positions.
	 *
	 * @param arguments the new contents to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(BlipContent... arguments);

	/**
	 * Inserts computed contents at the matched positions.
	 *
	 * @param functions the functions to compute the new contents based on the
	 *     matched contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(BlipContentFunction... functions);

	/**
	 * Inserts the given strings at the matched positions.
	 *
	 * @param arguments the new strings to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(String... arguments);

	/**
	 * Inserts the given arguments at the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new contents to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(List<BundledAnnotation> bundledAnnotations,
			BlipContent... arguments);

	/**
	 * Inserts computed contents at the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param functions the functions to compute the new contents based on the
	 *     matched contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(List<BundledAnnotation> bundledAnnotations,
			BlipContentFunction... functions);

	/**
	 * Inserts the given strings at the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new strings to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insert(List<BundledAnnotation> bundledAnnotations,
			String... arguments);

	/**
	 * Inserts the given arguments just after the matched positions.
	 *
	 * @param arguments the new contents to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(BlipContent... arguments);

	/**
	 * Inserts computed contents just after the matched positions.
	 *
	 * @param functions the functions to compute the new contents based on the
	 *     matched contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(BlipContentFunction... functions);

	/**
	 * Inserts the given strings just after the matched positions.
	 *
	 * @param arguments the new strings to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(String... arguments);

	/**
	 * Inserts the given arguments just after the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new contents to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(
			List<BundledAnnotation> bundledAnnotations,
			BlipContent... arguments);

	/**
	 * Inserts computed contents just after the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param functions the functions to compute the new contents based on the
	 *     matched contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(
			List<BundledAnnotation> bundledAnnotations,
			BlipContentFunction... functions);

	/**
	 * Inserts the given strings just after the matched positions.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new strings to be inserted.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs insertAfter(
			List<BundledAnnotation> bundledAnnotations, String... arguments);

	/**
	 * Replaces the matched positions with the given arguments.
	 *
	 * @param arguments the new contents to replace the original contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(BlipContent... arguments);

	/**
	 * Replaces the matched positions with computed contents.
	 *
	 * @param functions the functions to compute the new contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(BlipContentFunction... functions);

	/**
	 * Replaces the matched positions with the given strings.
	 *
	 * @param arguments the new strings to replace the original contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(String... arguments);

	/**
	 * Replaces the matched positions with the given arguments.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new contents to replace the original contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(List<BundledAnnotation> bundledAnnotations,
			BlipContent... arguments);

	/**
	 * Replaces the matched positions with computed contents.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param functions the functions to compute the new contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(List<BundledAnnotation> bundledAnnotations,
			BlipContentFunction... functions);

	/**
	 * Replaces the matched positions with the given strings.
	 *
	 * @param bundledAnnotations annotations to immediately apply to the inserted
	 *     text.
	 * @param arguments the new strings to replace the original contents.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs replace(List<BundledAnnotation> bundledAnnotations,
			String... arguments);

	/**
	 * Deletes the contents at the matched positions.
	 *
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs delete();

	/**
	 * Annotates the contents at the matched positions.
	 *
	 * @param key the annotation key.
	 * @param values the annotation values.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs annotate(String key, String... values);

	/**
	 * Clears the annotations at the matched positions.
	 *
	 * @param key the annotation key to be cleared.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs clearAnnotation(String key);

	/**
	 * Updates the properties of all elements at the matched positions with the
	 * given properties map.
	 *
	 * Note: The purpose of this overloaded version is because the version that
	 * takes a var-args generates compiler warning due to the way generics and
	 * var-args are implemented in Java. Most of the time, robot only needs to
	 * update one gadget at at time, and it can use this version to avoid the
	 * compiler warning.
	 *
	 * @param newProperties the new properties map.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs updateElement(Map<String, String> newProperties);

	/**
	 * Updates the properties of all elements at the matched positions with
	 * computed properties map.
	 *
	 * Note: The purpose of this overloaded version is because the version that
	 * takes a var-args generates compiler warning due to the way generics and
	 * var-args are implemented in Java. Most of the time, robot only needs to
	 * update one gadget at at time, and it can use this version to avoid the
	 * compiler warning.
	 *
	 * @param function the function to compute the new properties map.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs updateElement(MapFunction function);

	/**
	 * Updates the properties of all elements at the matched positions with the
	 * given properties maps.
	 *
	 * @param newProperties an array of new properties map.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs updateElement(Map<String, String>... newProperties);

	/**
	 * Updates the properties of all elements at the matched positions with
	 * computed properties maps.
	 *
	 * @param functions an array of function to compute new properties maps.
	 * @return an instance of this blip references, for chaining.
	 */
	public BlipContentRefs updateElement(MapFunction... functions);

	/**
	 * Checks whether this blip references contains any matches or not.
	 *
	 * @return {@code true} if it has any more matches. Otherwise, returns
	 *     {@code false}.
	 */
	public boolean isEmpty();

	/**
	 * Returns all matches.
	 *
	 * @return a list of {@link BlipContent} that represents the hits.
	 */
	public List<BlipContent> values();

	/**
	 * Returns the first hit.
	 *
	 * @return an instance of {@link BlipContent}, that represents the first hit.
	 */
	public BlipContent value();

	public Iterator<Range> iterator();

}
