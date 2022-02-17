package org.gabriel.annotations.type;

class Box<@NonEmpty T> {
  @NonEmpty int size;
  T type;

  public Box(final int size, final T type) {
    this.size = size;
    this.type = type;
  }

  class NestedBox extends Box<T> {
    public NestedBox(final int size, final @NonEmpty T type) {
      super(size, type);
    }
  }

}
