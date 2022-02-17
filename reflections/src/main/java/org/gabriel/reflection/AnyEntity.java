package org.gabriel.reflection;

class AnyEntity {

  public String type;
  private int val;

  private AnyEntity() {
    this(0, "id");
  }

  public AnyEntity(final int val, final String type) {
    this.val = val;
    this.type = type;
  }

  public int getVal() {
    return this.val;
  }

  private void setVal(final int val) {
    this.val = val;
  }

  public String getType() {
    return this.type;
  }

  @Override public String toString() {
    return "AnyEntity{" +
           "val=" + this.val +
           ", type='" + this.type + '\'' +
           '}';
  }
}
