package org.gabriel.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReadingAnnotations {

  public static void main(final String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    final Class<?> aClass = Class.forName("org.gabriel.demo.Utility");
    final Constructor<?> constructor = aClass.getConstructor();
    final Utility utility = (Utility) constructor.newInstance();
    final Method[] methods = aClass.getDeclaredMethods();

    for(final var method : methods) {
      if(method.isAnnotationPresent(MostUsed.class)) {
        final var mostUsed = method.getAnnotation(MostUsed.class);
        final var value = mostUsed.value();
        // execute method doStuff using annotation value as parameter
        method.invoke(utility, value);
      }
    }
  }

}
