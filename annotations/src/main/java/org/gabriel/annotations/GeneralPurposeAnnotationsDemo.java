package org.gabriel.annotations;


import java.util.ArrayList;

public class GeneralPurposeAnnotationsDemo {

  public static void main(final String[] args) {
    @SuppressWarnings("unused") final int a = 10;
    @SuppressWarnings({"rawtypes", "unused"}) final ArrayList aList = new ArrayList();
    @SuppressWarnings("deprecated") final Integer i = new Integer(0);

    final MyInterface myInterface = () -> System.out.println("Method invoked");
    myInterface.method();
  }

  @FunctionalInterface
  interface MyInterface {
    void method();
  }

  class Parent {
    public void m1() {
      System.out.println("m1 parent implementation");
    }

    public void m2() {
      System.out.println("m2 parent implementation");
    }
  }

  class Children extends Parent {

    @Override public void m1() {
      System.out.println("m1 child implementation");
    }
  }

}
