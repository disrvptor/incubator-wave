package com.google.wave.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Initial annotations to be applied to the document modification range.
 * disrvptor - extracted from BlipContentRefs also referenced in DocumentModifyAction
 */
public class BundledAnnotation {
  public String key;
  public String value;


  /**
   * Convenience method to create a list of bundled annotations with
   * the even values passed being the keys, the uneven ones the values.
   */
  public static List<BundledAnnotation> listOf(String... values) {
    if (values.length % 2 != 0) {
      throw new IllegalArgumentException("listOf takes an even number of parameters");
    }
    List<BundledAnnotation> res = new ArrayList<BundledAnnotation>(values.length / 2);
    for (int i = 0; i < values.length - 1; i += 2) {
      BundledAnnotation next = new BundledAnnotation();
      next.key = values[i];
      next.value = values[i + 1];
      res.add(next);
    }
    return res;
  }
}