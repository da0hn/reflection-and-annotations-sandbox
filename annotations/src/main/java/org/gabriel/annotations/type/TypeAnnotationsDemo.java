package org.gabriel.annotations.type;

class TypeAnnotationsDemo {

  public static void main(final String[] args) {
    final Box<String> box = new @NonEmpty @ReadOnly Box<>(10, "Container");

    box.new @ReadOnly NestedBox(11, "Cylinder");

  }

}
