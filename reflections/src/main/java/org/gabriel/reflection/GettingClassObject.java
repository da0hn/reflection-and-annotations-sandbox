package org.gabriel.reflection;

class MyClass {
  MyClass() {
    System.out.println("MyClass created!");
  }

}


class GettingClassObject {

  public static void main(final String[] args) throws ClassNotFoundException {
    // forName()
    final var aClass1 = Class.forName("java.lang.String");
    final var aClass2 = Class.forName("java.lang.String");

    System.out.println(aClass1 == aClass2);

    // ClassName.class
    final var integerClass = int.class;
    final var stringClass = String.class;

    // obj.getClass()
    final var myClass = new MyClass();
    final Class<? extends MyClass> aClass = myClass.getClass();

    // Super Class
    final Class<?> superClass = aClass.getSuperclass();

    // Interfaces
    final Class<?>[] interfaces = aClass.getInterfaces();

    // aClass.getName()
    System.out.println(aClass.getName());
  }

}
