package com.google.wave.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.wave.api.Function.BlipContentFunction;
import com.google.wave.api.Function.MapFunction;
import com.google.wave.api.JsonRpcConstant.ParamsProperty;
import com.google.wave.api.OperationRequest.Parameter;
import com.google.wave.api.impl.DocumentModifyAction;
import com.google.wave.api.impl.DocumentModifyAction.ModifyHow;
import com.google.wave.api.impl.DocumentModifyQuery;

public class BlipContentRefsImpl implements Iterable<Range>, BlipContentRefs {

	  /** The blip that this blip references are pointing to. */
	  private final Blip blip;

	  /** The iterator to iterate over the blip content. */
	  private final BlipIterator<?> iterator;

	  /** The additional parameters that need to be supplied in the outgoing op. */
	  private final List<Parameter> parameters;

	  /**
	   * Constructs an instance representing the search for text {@code target}.
	   *
	   * @param blip the blip to find {@code target} in.
	   * @param target the target to search.
	   * @param maxResult the maximum number of results.
	   * @return an instance of blip references.
	   */
	  public static BlipContentRefs all(Blip blip, String target, int maxResult) {
	    return new BlipContentRefsImpl(blip,
	        new BlipIterator.TextIterator(blip, target, maxResult),
	        Parameter.of(ParamsProperty.MODIFY_QUERY, new DocumentModifyQuery(target, maxResult)));
	  }

	  /**
	   * Constructs an instance representing the search for element
	   * {@code ElementType}, that has the properties specified in
	   * {@code restrictions}.
	   *
	   * @param blip the blip to find {@code target} in.
	   * @param target the element type to search.
	   * @param maxResult the maximum number of results.
	   * @param restrictions the additional properties filter that need to be
	   *     matched.
	   * @return an instance of blip references.
	   */
	  public static BlipContentRefs all(Blip blip, ElementType target, int maxResult,
	      Restriction... restrictions) {
	    Map<String, String> restrictionsAsMap = new HashMap<String, String>(restrictions.length);
	    for (Restriction restriction : restrictions) {
	      restrictionsAsMap.put(restriction.getKey(), restriction.getValue());
	    }

	    return new BlipContentRefsImpl(blip,
	        new BlipIterator.ElementIterator(blip, target, restrictionsAsMap, maxResult),
	        Parameter.of(ParamsProperty.MODIFY_QUERY,
	            new DocumentModifyQuery(target, restrictionsAsMap, maxResult)));
	  }

	  /**
	   * Constructs an instance representing the entire blip content.
	   *
	   * @param blip the blip to represent.
	   * @return an instance of blip references.
	   */
	  public static BlipContentRefs all(Blip blip) {
	    return new BlipContentRefsImpl(blip,
	        new BlipIterator.SingleshotIterator(blip, 0, blip.getContent().length()));
	  }

	  /**
	   * Constructs an instance representing an explicitly set range.
	   *
	   * @param blip the blip to represent.
	   * @param start the start index of the range.
	   * @param end the end index of the range.
	   * @return an instance of blip references.
	   */
	  public static BlipContentRefs range(Blip blip, int start, int end) {
	    return new BlipContentRefsImpl(blip,
	        new BlipIterator.SingleshotIterator(blip, start, end),
	        Parameter.of(ParamsProperty.RANGE, new Range(start, end)));
	  }

	  /**
	   * Private constructor.
	   *
	   * @param blip the blip to navigate.
	   * @param iterator the iterator to iterate over blip content.
	   * @param parameters the additional parameters to be passed in the outgoing
	   *     operation.
	   */
	  private BlipContentRefsImpl(Blip blip, BlipIterator<?> iterator, Parameter... parameters) {
	    this.blip = blip;
	    this.iterator = iterator;
	    this.parameters = Arrays.asList(parameters);
	  }

	  /**
	   * Executes this BlipRefs object.
	   *
	   * @param modifyHow the operation to be executed.
	   * @param bundledAnnotations optional list of annotations to immediately
	   *     apply to newly added text.
	   * @param arguments a list of arguments for the operation. The arguments vary
	   *     depending on the operation:
	   *     <ul>
	   *       <li>For DELETE: none (the supplied arguments will be ignored)</li>
	   *       <li>For ANNOTATE: a list of {@link Annotation} objects</li>
	   *       <li>For CLEAR_ANNOTATION: the key of the annotation to be
	   *           cleared. Only the first argument will be used.</li>
	   *       <li>For UPDATE_ELEMENT: a list of {@link Map}, each represents
	   *           new element properties.</li>
	   *       <li>For INSERT, INSERT_AFTER, or REPLACE: a list of
	   *           {@link BlipContent}s.
	   *     </ul>
	   *     For operations that take a list of entities as the argument, once
	   *     this method hits the end of the argument list, it will wrap around.
	   *     For example, if this {@link BlipContentRefs} object has 5 hits, when
	   *     applying an ANNOTATE operation with 4 arguments, the first argument
	   *     will be applied to the 5th hit.
	   * @return this instance of blip references, for chaining.
	   */
	  @SuppressWarnings({"unchecked", "fallthrough"})
	  private BlipContentRefs execute(
	      ModifyHow modifyHow, List<BundledAnnotation> bundledAnnotations, Object... arguments) {
	    // If there is no match found, return immediately without generating op.
	    if (!iterator.hasNext()) {
	      return this;
	    }

	    int nextIndex = 0;
	    Object next = null;
	    List<BlipContent> computed = new ArrayList<BlipContent>();
	    List<Element> updatedElements = new ArrayList<Element>();
	    boolean useMarkup = false;

	    while (iterator.hasNext()) {
	      Range range = iterator.next();
	      int start = range.getStart();
	      int end = range.getEnd();

	      if (blip.length() == 0 && (start != 0 || end != 0)) {
	        throw new IndexOutOfBoundsException("Start and end have to be 0 for empty blip.");
	      } else if (start < 0 || end < 1) {
	        throw new IndexOutOfBoundsException("Position outside the blip.");
	      } else if ((start >= blip.length() || end > blip.length()) &&
	          modifyHow != ModifyHow.INSERT) {
	        throw new IndexOutOfBoundsException("Position outside the blip.");
	      } else if (start > blip.length() && modifyHow == ModifyHow.INSERT) {
	        throw new IndexOutOfBoundsException("Position outside the blip.");
	      } else if (start >= end){
	        throw new IndexOutOfBoundsException("Start has to be less than end.");
	      }

	      // Get the next argument.
	      if (nextIndex < arguments.length) {
	        next = arguments[nextIndex];

	        // If the next argument is a function, call the function.
	        if (next instanceof Function) {
	          // Get the matched content.
	          BlipContent source;
	          if (end - start == 1 && blip.getElements().containsKey(start)) {
	            source = blip.getElements().get(start);
	          } else {
	            source = Plaintext.of(blip.getContent().substring(start, end));
	          }
	          // Compute the new content.
	          next = ((Function) next).call(source);
	          if (next instanceof BlipContent) {
	            computed.add((BlipContent) next);
	          }
	        }
	        nextIndex = ++nextIndex % arguments.length;
	      }

	      switch (modifyHow) {
	        case DELETE:
	          // You can't delete the first newline.
	          if (start == 0) {
	            start = 1;
	          }

	          // Delete all elements that fall into this range.
	          Iterator<Integer> elementIterator =
	              blip.getElements().subMap(start, end).keySet().iterator();
	          while(elementIterator.hasNext()) {
	            elementIterator.next();
	            elementIterator.remove();
	          }

	          ((BlipImpl)blip).deleteAnnotations(start, end);
	          ((BlipImpl)blip).shift(end, start - end);
	          iterator.shift(-1);
	          blip.setContent(blip.getContent().substring(0, start) +
	              blip.getContent().substring(end));
	          break;
	        case ANNOTATE:
	          Annotation annotation = Annotation.class.cast(next);
	          blip.getAnnotations().add(annotation.getName(), annotation.getValue(), start, end);
	          break;
	        case CLEAR_ANNOTATION:
	          String annotationName = arguments[0].toString();
	          blip.getAnnotations().delete(annotationName, start, end);
	          break;
	        case UPDATE_ELEMENT:
	          Element existingElement = blip.getElements().get(start);
	          if (existingElement == null) {
	            throw new IllegalArgumentException("No element found at index " + start + ".");
	          }
	          Map<String, String> properties = Map.class.cast(next);
	          updatedElements.add(new Element(existingElement.getType(), properties));
	          for (Entry<String, String> entry : properties.entrySet()) {
	            existingElement.setProperty(entry.getKey(), entry.getValue());
	          }
	          break;
	        case INSERT:
	          end = start;
	        case INSERT_AFTER:
	          start = end;
	        case REPLACE:
	          // Get the plain-text version of next.
	          String text = BlipContent.class.cast(next).getText();

	          // Compute the shift amount for the iterator.
	          int iteratorShiftAmount = text.length() - 1;
	          if (end == start) {
	            iteratorShiftAmount += range.getEnd() - range.getStart();
	          }
	          iterator.shift(iteratorShiftAmount);

	          // In the case of a replace, and the replacement text is shorter,
	          // delete the delta.
	          if (start != end && text.length() < end - start) {
	        	  ((BlipImpl)blip).deleteAnnotations(start + text.length(), end);
	          }

	          ((BlipImpl)blip).shift(end, text.length() + start - end);
	          blip.setContent(blip.getContent().substring(0, start) + text +
	              blip.getContent().substring(end));

	          if (next instanceof Element) {
	            blip.getElements().put(start, Element.class.cast(next));
	          } else if (bundledAnnotations != null) {
	            for (BundledAnnotation bundled : bundledAnnotations) {
	              blip.getAnnotations().add(bundled.key, bundled.value, start, start + text.length());
	            }
	          }
	          break;
	      }
	    }

	    OperationRequest op = ((BlipImpl)blip).getOperationQueue().modifyDocument(blip);

	    for (Parameter parameter : parameters) {
	      op.addParameter(parameter);
	    }

	    // Prepare the operation parameters.
	    List<String> values = null;
	    String annotationName = null;
	    List<Element> elements = null;
	    switch (modifyHow) {
	      case UPDATE_ELEMENT:
	        elements = updatedElements;
	        break;
	      case ANNOTATE:
	        values = new ArrayList<String>(arguments.length);
	        for (Object item : arguments) {
	          values.add(Annotation.class.cast(item).getValue());
	        }
	        annotationName = Annotation.class.cast(arguments[0]).getName();
	        break;
	      case CLEAR_ANNOTATION:
	        annotationName = arguments[0].toString();
	        break;
	      case INSERT:
	      case INSERT_AFTER:
	      case REPLACE:
	        values = new ArrayList<String>(arguments.length);
	        elements = new ArrayList<Element>(arguments.length);
	        Object[] toBeAdded = arguments;
	        if (arguments[0] instanceof Function) {
	          toBeAdded = computed.toArray();
	        }
	        for (Object argument : toBeAdded) {
	          if (argument instanceof Element) {
	            elements.add(Element.class.cast(argument));
	            values.add(null);
	          } else if (argument instanceof Plaintext){
	            values.add(BlipContent.class.cast(argument).getText());
	            elements.add(null);
	          }
	        }
	        break;
	    }

	    op.addParameter(Parameter.of(ParamsProperty.MODIFY_ACTION,
	        new DocumentModifyAction(
	            modifyHow, values, annotationName, elements, bundledAnnotations, useMarkup)));

	    iterator.reset();
	    return this;
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insert(com.google.wave.api.BlipContent)
	 */
	  @Override
	public BlipContentRefs insert(BlipContent... arguments) {
	    return insert(null, arguments);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insert(com.google.wave.api.Function.BlipContentFunction)
	 */
	  @Override
	public BlipContentRefs insert(BlipContentFunction... functions) {
	    return insert(null, functions);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insert(java.lang.String)
	 */
	  @Override
	public BlipContentRefs insert(String... arguments) {
	    return insert(null, arguments);
	  }

	  /**
	   * Inserts the given arguments at the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new contents to be inserted.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insert(
	      List<BundledAnnotation> bundledAnnotations, BlipContent... arguments) {
	    return execute(ModifyHow.INSERT, bundledAnnotations, ((Object[]) arguments));
	  }

	  /**
	   * Inserts computed contents at the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param functions the functions to compute the new contents based on the
	   *     matched contents.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insert(
	      List<BundledAnnotation> bundledAnnotations, BlipContentFunction... functions) {
	    return execute(ModifyHow.INSERT, bundledAnnotations, ((Object[]) functions));
	  }

	  /**
	   * Inserts the given strings at the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new strings to be inserted.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insert(List<BundledAnnotation> bundledAnnotations, String... arguments) {
	    Object[] array = new Plaintext[arguments.length];
	    for (int i = 0; i < arguments.length; ++i) {
	      array[i] = Plaintext.of(arguments[i]);
	    }
	    return execute(ModifyHow.INSERT, bundledAnnotations, array);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insertAfter(com.google.wave.api.BlipContent)
	 */
	  @Override
	public BlipContentRefs insertAfter(BlipContent... arguments) {
	    return insertAfter(null, arguments);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insertAfter(com.google.wave.api.Function.BlipContentFunction)
	 */
	  @Override
	public BlipContentRefs insertAfter(BlipContentFunction... functions) {
	    return insertAfter(null, functions);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#insertAfter(java.lang.String)
	 */
	  @Override
	public BlipContentRefs insertAfter(String... arguments) {
	    return insertAfter(null, arguments);
	  }

	  /**
	   * Inserts the given arguments just after the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new contents to be inserted.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insertAfter(
	      List<BundledAnnotation> bundledAnnotations, BlipContent... arguments) {
	    return execute(ModifyHow.INSERT_AFTER, bundledAnnotations, (Object[]) arguments);
	  }

	  /**
	   * Inserts computed contents just after the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param functions the functions to compute the new contents based on the
	   *     matched contents.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insertAfter(
	      List<BundledAnnotation> bundledAnnotations, BlipContentFunction... functions) {
	    return execute(ModifyHow.INSERT_AFTER, bundledAnnotations, (Object[]) functions);
	  }

	  /**
	   * Inserts the given strings just after the matched positions.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new strings to be inserted.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs insertAfter(
	      List<BundledAnnotation> bundledAnnotations, String... arguments) {
	    Object[] array = new Plaintext[arguments.length];
	    for (int i = 0; i < arguments.length; ++i) {
	      array[i] = Plaintext.of(arguments[i]);
	    }
	    return execute(ModifyHow.INSERT_AFTER, bundledAnnotations, array);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#replace(com.google.wave.api.BlipContent)
	 */
	  @Override
	public BlipContentRefs replace(BlipContent... arguments) {
	    return replace(null, arguments);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#replace(com.google.wave.api.Function.BlipContentFunction)
	 */
	  @Override
	public BlipContentRefs replace(BlipContentFunction... functions) {
	    return replace(null, functions);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#replace(java.lang.String)
	 */
	  @Override
	public BlipContentRefs replace(String... arguments) {
	    return replace(null, arguments);
	  }

	  /**
	   * Replaces the matched positions with the given arguments.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new contents to replace the original contents.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs replace(
	      List<BundledAnnotation> bundledAnnotations, BlipContent... arguments) {
	    return execute(ModifyHow.REPLACE, bundledAnnotations, (Object[]) arguments);
	  }

	  /**
	   * Replaces the matched positions with computed contents.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param functions the functions to compute the new contents.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs replace(
	      List<BundledAnnotation> bundledAnnotations, BlipContentFunction... functions) {
	    return execute(ModifyHow.REPLACE, bundledAnnotations, (Object[]) functions);
	  }

	  /**
	   * Replaces the matched positions with the given strings.
	   *
	   * @param bundledAnnotations annotations to immediately apply to the inserted
	   *     text.
	   * @param arguments the new strings to replace the original contents.
	   * @return an instance of this blip references, for chaining.
	   */
	  @Override
	public BlipContentRefs replace(List<BundledAnnotation> bundledAnnotations, String... arguments) {
	    Object[] array = new Plaintext[arguments.length];
	    for (int i = 0; i < arguments.length; ++i) {
	      array[i] = Plaintext.of(arguments[i]);
	    }
	    return execute(ModifyHow.REPLACE, bundledAnnotations, array);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#delete()
	 */
	  @Override
	public BlipContentRefs delete() {
	    return execute(ModifyHow.DELETE, null);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#annotate(java.lang.String, java.lang.String)
	 */
	  @Override
	public BlipContentRefs annotate(String key, String... values) {
	    if (values.length == 0) {
	      values = new String[]{key};
	    }

	    Annotation[] annotations = new Annotation[values.length];
	    for (int i = 0; i < values.length; ++i) {
	      annotations[i] = new Annotation(key, values[i], 0, 1);
	    }
	    return execute(ModifyHow.ANNOTATE, null, (Object[]) annotations);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#clearAnnotation(java.lang.String)
	 */
	  @Override
	public BlipContentRefs clearAnnotation(String key) {
	    return execute(ModifyHow.CLEAR_ANNOTATION, null, key);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#updateElement(java.util.Map)
	 */
	  @Override
	public BlipContentRefs updateElement(Map<String, String> newProperties) {
	    return execute(ModifyHow.UPDATE_ELEMENT, null, new Object[] {newProperties});
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#updateElement(com.google.wave.api.Function.MapFunction)
	 */
	  @Override
	public BlipContentRefs updateElement(MapFunction function) {
	    return execute(ModifyHow.UPDATE_ELEMENT, null, new Object[] {function});
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#updateElement(java.util.Map)
	 */
	  @Override
	@SuppressWarnings("unchecked")
	  public BlipContentRefs updateElement(Map<String, String>... newProperties) {
	    return execute(ModifyHow.UPDATE_ELEMENT, null, (Object[]) newProperties);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#updateElement(com.google.wave.api.Function.MapFunction)
	 */
	  @Override
	public BlipContentRefs updateElement(MapFunction... functions) {
	    return execute(ModifyHow.UPDATE_ELEMENT, null, (Object[]) functions);
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#isEmpty()
	 */
	  @Override
	public boolean isEmpty() {
	    return iterator.hasNext();
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#values()
	 */
	  @Override
	public List<BlipContent> values() {
	    List<BlipContent> result = new ArrayList<BlipContent>();
	    while (iterator.hasNext()) {
	      Range range = iterator.next();
	      if (range.getEnd() - range.getStart() == 1 &&
	          blip.getElements().containsKey(range.getStart())) {
	        result.add(blip.getElements().get(range.getStart()));
	      } else {
	        result.add(Plaintext.of(blip.getContent().substring(range.getStart(), range.getEnd())));
	      }
	    }
	    iterator.reset();
	    return result;
	  }

	  /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#value()
	 */
	  @Override
	public BlipContent value() {
	    BlipContent result = null;
	    if (iterator.hasNext()) {
	      Range range = iterator.next();
	      if (range.getEnd() - range.getStart() == 1 &&
	          blip.getElements().containsKey(range.getStart())) {
	        result = blip.getElements().get(range.getStart());
	      } else {
	        result = Plaintext.of(blip.getContent().substring(range.getStart(), range.getEnd()));
	      }
	    }
	    iterator.reset();
	    return result;
	}

    /* (non-Javadoc)
	 * @see com.google.wave.api.BlipContentRefsImpl#iterator()
	 */
	@Override
	public Iterator<Range> iterator() {
	    iterator.reset();
	    return iterator;
	}

}
