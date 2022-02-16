package org.gabriel.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class AnyClass {

  private AnyClass() {
    System.out.println("AnyClass Object created!");
  }

}


class ReflectionDemo {

  public static void main(final String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    //    var obj = new AnyClass(); -> error
    final Class<?> aClass = Class.forName("org.gabriel.reflection.AnyClass");
    final Constructor<?> constructor = aClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    final var anyClassInstance = (AnyClass) constructor.newInstance();

  }

}
