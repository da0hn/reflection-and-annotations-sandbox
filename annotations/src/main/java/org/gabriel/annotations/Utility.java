package org.gabriel.annotations;

@MostUsed
public class Utility {

  void doStuff() {
    System.out.println("Doing something");
  }

  @MostUsed("Python")
  void doStuff(final String str) {
    System.out.println("Operation on: " + str);
  }

  void doStuff(final int number) {
    System.out.println("Operating on: " + number);
  }

}

