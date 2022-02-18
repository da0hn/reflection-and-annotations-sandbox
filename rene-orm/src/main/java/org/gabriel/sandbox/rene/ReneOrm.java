package org.gabriel.sandbox.rene;

import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ReneOrm<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReneOrm.class);

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
        LOGGER.info("Found primary key '{}'", field.getName());
      }
      else {
        LOGGER.info("Found column '{}'", field.getName());
      }
    }

    final String insertStatement = """
                                   INSERT INTO %s (%s) VALUES (%s)
                                   """.formatted(aClass.getSimpleName(), "", "");

    LOGGER.info("Generated insert statement: {}", insertStatement);

  }
}
