package org.gabriel.sandbox;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

public class Main {

  public static void main(final String[] args) throws Throwable {
    final var lookup = MethodHandles.lookup();

    final Class<?> studentClass = lookup.findClass(Student.class.getName());

    final var methodType = MethodType.methodType(String.class);

    final var getCourseHandle = lookup.findVirtual(Student.class, "getCourse", methodType);

    final Student student = new Student();
    student.setCourse("Java");

    System.out.println(getCourseHandle.invoke(student));

    final var constructorMethodType = MethodType.methodType(void.class);
    final var noArgConstructorHandle = lookup.findConstructor(studentClass, constructorMethodType);

    final var studentUsingMethodHandle = (Student) noArgConstructorHandle.invoke();

    studentUsingMethodHandle.setName("Name");
    studentUsingMethodHandle.setCourse("Course");
    System.out.println(studentUsingMethodHandle);

    final var constructorWithParametersMethodType = MethodType.methodType(void.class, String.class, String.class);
    final var constructorWithArgsHandle = lookup.findConstructor(studentClass, constructorWithParametersMethodType);

    final var studentUsingConstructorHandleWithParameters = constructorWithArgsHandle.invoke("Gabriel", "Kotlin");
    System.out.println(studentUsingConstructorHandleWithParameters);

    final var setNameMethodType = MethodType.methodType(void.class, String.class);
    final var setNameHandler = lookup.findVirtual(studentClass, "setName", setNameMethodType);

    setNameHandler.invoke(student, "edited using method handle");
    System.out.println(student);

    final var methodType1 = MethodType.methodType(void.class, int.class);
    final var setNumOfStudentsHandle = lookup.findStatic(studentClass, "setNumOfStudents", methodType1);
    setNumOfStudentsHandle.invoke(10); // Static method not require Student.class
    System.out.println(Student.getNumOfStudents() == 10);

    final var privateLookup = MethodHandles.privateLookupIn(studentClass, lookup);

    final var getNameHandle = privateLookup.findGetter(studentClass, "name", String.class);
    final var studentName = getNameHandle.invoke(student);
    System.out.println(studentName);

    // VarHandles

    final var courseVarHandle = privateLookup.findVarHandle(studentClass, "course", String.class);
    courseVarHandle.set(student, "Edited Using VarHandle");
    final String courseGettedByVarHandle = (String)courseVarHandle.get(student);
    System.out.println(courseGettedByVarHandle);
  }

}
