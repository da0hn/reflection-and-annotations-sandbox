package org.gabriel.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.gabriel.reflection.DebugUtil.after;
import static org.gabriel.reflection.DebugUtil.before;
import static org.gabriel.reflection.DebugUtil.separator;

class GetMethodMetadata {

  public static void main(final String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    final var anyEntity = new AnyEntity(10, "10");
    final Class<? extends AnyEntity> aClass = anyEntity.getClass();
    separator();
    printPublicMethods(aClass);
    separator();
    printDeclaredMethods(aClass);
    separator();

    before(anyEntity);
    System.out.println("Getting private method 'setVal'...");
    final Method setVal = aClass.getDeclaredMethod("setVal", int.class);
    setVal.setAccessible(true);
    System.out.println("Setting 'setVal' as accessible");
    setVal.invoke(anyEntity, 15); // execute method
    System.out.println("invoking method...");
    after(anyEntity);

    System.out.println("Getting public method 'getVal'...");
    final Method getVal = aClass.getMethod("getVal");
    System.out.println("invoking method...");
    final var result = (int) getVal.invoke(anyEntity);
    System.out.println("Returned value: " + result);
  }


  private static void printPublicMethods(final Class<? extends AnyEntity> aClass) {
    final Method[] methods = aClass.getMethods();

    for(final var method : methods) {
      System.out.println(method.getName());
    }
  }

  private static void printDeclaredMethods(final Class<? extends AnyEntity> aClass) {
    final Method[] methods = aClass.getDeclaredMethods();

    for(final var method : methods) {
      System.out.println(method.getName());
    }
  }
}
