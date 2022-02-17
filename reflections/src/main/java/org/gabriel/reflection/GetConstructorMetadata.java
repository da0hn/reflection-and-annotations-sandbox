package org.gabriel.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.gabriel.reflection.DebugUtil.separator;

class GetConstructorMetadata {

  public static void main(final String... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    final Class<?> anyEntityClass = Class.forName("org.gabriel.reflection.AnyEntity");

    printPublicConstructors(anyEntityClass);
    separator();
    printDeclaredConstructors(anyEntityClass);
    separator();
    instantiateUsingPublicConstructor(anyEntityClass);
    separator();
    instantiateUsingPrivateConstructor(anyEntityClass);
  }

  private static void instantiateUsingPrivateConstructor(final Class<?> anyEntityClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    System.out.println("Getting a private no-arg constructor...");
    final Constructor<?> privateConstructor = anyEntityClass.getDeclaredConstructor();
    System.out.println("Setting private constructor as 'accessible'...");
    privateConstructor.setAccessible(true);
    System.out.println("Instantiate 'AnyEntity' using reflection");
    final var anotherAnyEntity = (AnyEntity) privateConstructor.newInstance();
    System.out.println("Object instantiated successfully: " + anotherAnyEntity);
  }

  private static void instantiateUsingPublicConstructor(final Class<?> anyEntityClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    System.out.println("Getting a constructor with int and string parameter");
    final Constructor<?> constructor = anyEntityClass.getConstructor(int.class, String.class);
    System.out.println("Instantiate 'AnyEntity' using reflection");
    final var anyEntity = (AnyEntity) constructor.newInstance(10, "StudentId");
    System.out.println("Object instantiated successfully: " + anyEntity);
  }

  private static void printDeclaredConstructors(final Class<?> anyEntityClass) {
    System.out.println("Declared constructors list: ");
    final Constructor<?>[] constructors = anyEntityClass.getDeclaredConstructors();
    for(final var constructor : constructors) {
      System.out.println(constructor);
    }
  }

  private static void printPublicConstructors(final Class<?> anyEntityClass) {
    System.out.println("Public constructors list: ");
    final Constructor<?>[] constructors = anyEntityClass.getConstructors();
    for(final var constructor : constructors) {
      System.out.println(constructor);
    }
  }

}
