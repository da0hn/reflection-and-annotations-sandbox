package org.gabriel.reflection;

import static org.gabriel.reflection.DebugUtil.after;
import static org.gabriel.reflection.DebugUtil.before;
import static org.gabriel.reflection.DebugUtil.separator;

class GetFieldMetadata {

  public static void main(final String[] args) throws NoSuchFieldException, IllegalAccessException {
    final var entity = new AnyEntity(10, "id");
    final Class<? extends AnyEntity> aClass = entity.getClass();

    // non-declared fields : All the public elements in that class and its super class
    // declared : All the elements present in that class only.
    printPublicFields(aClass);
    separator();
    printDeclaredFields(aClass);

    separator();

    before(entity);
    final var field = aClass.getField("type");
    field.set(entity, "rollNo.");
    after(entity);

    separator();

    before(entity);
    // Field field = aClass.getField("val") -> runtime error, "val" is private
    final var field2 = aClass.getDeclaredField("val");
    field2.setAccessible(true); // runtime error if field has modifiers and not accessible
    field2.set(entity, 19);
    after(entity);
  }


  private static void printPublicFields(final Class<? extends AnyEntity> aClass) {
    final var fields = aClass.getFields();
    for(final var field : fields) {
      System.out.println(field.getName());
    }
  }

  private static void printDeclaredFields(final Class<? extends AnyEntity> aClass) {
    final var declaredFields = aClass.getDeclaredFields();
    for(final var field : declaredFields) {
      System.out.println(field.getName());
    }
  }

}
