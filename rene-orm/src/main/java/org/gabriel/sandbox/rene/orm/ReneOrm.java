package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

    final var columns = this.getColumns(aClass);
    final var pkColumn = this.getPrimaryKey(aClass);

    final String formattedName = this.getTableName(aClass);

    final String insertStatement = """
                                   INSERT INTO %s (%s) VALUES (%s)
                                   """.formatted(formattedName, "", "");

    LOGGER.info(insertStatement);

  }

  private String getTableName(final Class<?> aClass) {
    final var hasTableAnnotation = aClass.isAnnotationPresent(Table.class);

    if(!hasTableAnnotation) throw new IllegalStateException();

    final var tableAnnotation = aClass.getAnnotation(Table.class);
    final var tableName = tableAnnotation.name();

    return tableName.isEmpty() ? aClass.getSimpleName().toLowerCase(Locale.ROOT) : tableName;
  }

  private List<Field> getColumns(final Class<?> aClass) {
    return Arrays.stream(aClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Column.class))
      .peek(column -> LOGGER.info("Found column '{}'", column.getName()))
      .toList();
  }

  private Field getPrimaryKey(final Class<?> aClass) {
    return Arrays.stream(aClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class))
      .peek(pk -> LOGGER.info("Found primary key '{}'", pk.getName()))
      .findFirst()
      .orElseThrow();
  }
}
