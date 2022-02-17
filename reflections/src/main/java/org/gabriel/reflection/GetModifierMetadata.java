package org.gabriel.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.gabriel.reflection.DebugUtil.separator;

class GetModifierMetadata {

  public static void main(final String[] args) throws NoSuchMethodException {

    final var anyEntity = new AnyEntity(10, "id");
    final Class<? extends AnyEntity> aClass = anyEntity.getClass();
    System.out.println("Getting modifier of class 'AnyEntity'");
    final int modifiers = aClass.getModifiers();
    final var isPublicClass = modifiers & Modifier.PUBLIC;
    System.out.println("Verifying method modifier: ");
    System.out.println("Is public class: " + isPublicClass);
    separator();
    System.out.println("Getting method 'getVal' using reflection");
    final Method method = aClass.getMethod("getVal");
    System.out.println("Getting modifier of 'getVal'");
    final var methodModifiers = method.getModifiers();
    final var isPrivateMethod = methodModifiers & Modifier.PRIVATE;
    System.out.println("Verifying method modifier: ");
    System.out.println("Is private method: " + isPrivateMethod);
    System.out.println("Is public method: " + Modifier.isPublic(methodModifiers));
    System.out.println("Current modifier as String: " + Modifier.toString(methodModifiers));
  }

}
