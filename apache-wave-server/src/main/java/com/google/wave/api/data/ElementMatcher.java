package com.google.wave.api.data;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.wave.api.ElementType;
import com.google.wave.api.Range;

/**
   * ElementMatcher yields all ranges in the document matching the searched for
   * element type.
   */
  public class ElementMatcher implements DocumentHitIterator {

    private int hitsLeft;
    private int index;
    private final ApiView apiView;
    private final ElementType elementType;
    private final Map<String, String> restrictions;

    public ElementMatcher(
        ApiView apiView, ElementType elementType, Map<String, String> restrictions, int maxRes) {
      Preconditions.checkNotNull(apiView, "Api view must not be null");
      Preconditions.checkNotNull(elementType, "The type of element to search for must not be null");
      Preconditions.checkNotNull(restrictions, "The search restricitions must not be null");

      this.elementType = elementType;
      this.apiView = apiView;
      this.index = -1;
      this.hitsLeft = maxRes;
      this.restrictions = restrictions;
    }

    @Override
    public Range next() {
      if (hitsLeft == 0) {
        return null;
      }
      hitsLeft--;

      for (ApiView.ElementInfo elementInfo : apiView.getElements()) {
        if (elementInfo.element.getType().equals(elementType) && elementInfo.apiPosition > index) {
          boolean allMatched = true;
          for (Entry<String, String> entry : restrictions.entrySet()) {
            if (!entry.getValue().equals(elementInfo.element.getProperty(entry.getKey()))) {
              allMatched = false;
              break;
            }
          }
          if (!allMatched) {
            continue;
          }
          index = elementInfo.apiPosition;
          return new Range(index, index + 1);
        }
      }
      return null;
    }

    @Override
    public void shift(int where, int delta) {
    }
  }