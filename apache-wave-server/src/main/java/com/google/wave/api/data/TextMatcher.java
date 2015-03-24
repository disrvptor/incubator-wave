package com.google.wave.api.data;

import com.google.common.base.Preconditions;
import com.google.wave.api.Range;

/**
   * TextMatcher yields all ranges in the document matching the searched for
   * string.
   */
  public class TextMatcher implements DocumentHitIterator {

    private final ApiView apiView;
    private final String searchFor;
    private int from;
    private int hitsLeft;

    public TextMatcher(ApiView apiView, String searchFor, int maxHits) {
      Preconditions.checkNotNull(apiView, "Api view must not be null");
      Preconditions.checkNotNull(searchFor, "The string to search for must not be null");

      this.apiView = apiView;
      this.searchFor = searchFor;
      this.from = -1;
      this.hitsLeft = maxHits;
    }

    @Override
    public Range next() {
      if (hitsLeft == 0) {
        return null;
      }
      hitsLeft--;
      String searchIn = apiView.apiContents();
      int next = searchIn.indexOf(searchFor, from + 1);
      if (next == -1) {
        return null;
      }
      from = next;
      return new Range(from, from + searchFor.length());
    }

    @Override
    public void shift(int where, int delta) {
      if (from != -1) {
        if (where - 1 <= from) {
          from += delta;
        }
      }
    }
  }