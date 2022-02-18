package org.gabriel.sandbox.rene;

import org.gabriel.sandbox.rene.annotations.PrimaryKey;

import java.sql.Connection;
import java.sql.SQLException;

public class ReneOrm<T> {


  private final Connection connection;

  private ReneOrm() throws SQLException {
    //    this.connection = DriverManager.getConnection("");
    this.connection = null;
  }

  public static <T> ReneOrm<T> getConnection() throws SQLException {
    return new ReneOrm<>();
  }

  public void write(final T entity) {
    final Class<?> aClass = entity.getClass();
    final var fields = aClass.getDeclaredFields();
    for(final var field : fields) {
      if(field.isAnnotationPresent(PrimaryKey.class)) {
        System.out.println("The primary key is: " + field.getName());
      }
      else {
        System.out.println("Found column: " + field.getName());
      }
    }
  }
}
