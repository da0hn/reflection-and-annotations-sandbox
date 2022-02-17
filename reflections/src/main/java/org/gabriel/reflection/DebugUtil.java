package org.gabriel.reflection;

class DebugUtil {

  private DebugUtil() {
  }


  static void separator() {
    System.out.println("-".repeat(40));
  }

  static void before(Object object) {
    System.out.println("Before: " + object);
  }

  static void after(Object object) {
    System.out.println("After: " + object);
  }
}
