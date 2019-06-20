package it.polimi.se2019.adrenalina.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Exclusion strategy for {@code @NotExpose} annotation.
 */
public class NotExposeExclusionStrategy implements ExclusionStrategy {
  @Override
  public boolean shouldSkipField(FieldAttributes fieldAttributes) {
    return fieldAttributes.getAnnotation(NotExpose.class) != null;
  }

  @Override
  public boolean shouldSkipClass(Class<?> aClass) {
    return aClass.isAnnotationPresent(NotExpose.class);
  }
}
