package org.gabriel.sandbox.rene.orm;

import java.util.function.Supplier;

class StringUtil {

  static String requireNonEmptyOrElseGet(final String value, final Supplier<String> defaultValue) {
    return value.isEmpty() ? defaultValue.get() : value;
  }

}
