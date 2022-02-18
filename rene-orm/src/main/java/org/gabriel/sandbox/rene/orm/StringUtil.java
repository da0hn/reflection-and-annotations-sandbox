package org.gabriel.sandbox.rene.orm;

import java.util.function.Supplier;

public class StringUtil {

  public static String requireNonEmptyOrElseGet(final String value, final Supplier<String> defaultValue) {
    return value.isEmpty() ? defaultValue.get() : value;
  }

}
