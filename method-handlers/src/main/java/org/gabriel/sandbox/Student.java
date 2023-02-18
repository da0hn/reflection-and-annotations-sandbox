package org.gabriel.sandbox;

public class Student {

  private static int numOfStudents;
  private String name;
  private String course;

  public Student() {
    numOfStudents++;
  }

  public Student(
    final String name,
    final String course
  ) {
    numOfStudents++;
    this.name = name;
    this.course = course;
  }

  public static int getNumOfStudents() {
    return numOfStudents;
  }

  public static void setNumOfStudents(final int numOfStudents) {
    Student.numOfStudents = numOfStudents;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getCourse() {
    return this.course;
  }

  public void setCourse(final String course) {
    this.course = course;
  }

  @Override
  public String toString() {
    return "Student{" +
           "name='" + name + '\'' +
           ", course='" + course + '\'' +
           '}';
  }

}
