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
import java.util.Optional;
import java.util.stream.Collectors;

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

    final var joinedColumnName = columns.stream()
      .map(this::getColumnName)
      .collect(Collectors.joining(", "));

    final var joinedColumnValue = columns.stream().map(this::getColumnValue).collect(Collectors.joining(", "));

    final String insertStatement = """
                                   INSERT INTO %s (%s) VALUES (%s)
                                   """.formatted(formattedName, joinedColumnName, joinedColumnValue);

    LOGGER.info(insertStatement);

  }

  private String getColumnValue(final Field field) {
    return null;
  }

  private String getColumnName(final Field field) {
    final var columnName = Optional.ofNullable(field.getAnnotation(Column.class))
      .map(Column::name)
      .orElseThrow(IllegalStateException::new);
    return StringUtil.requireNonEmptyOrElseGet(
      columnName,
      () -> field.getName().toLowerCase(Locale.ROOT)
    );
  }

  private String getTableName(final Class<?> aClass) {
    final var tableName = Optional.ofNullable(aClass.getAnnotation(Table.class))
      .map(Table::name)
      .orElseThrow(IllegalStateException::new);

    return StringUtil.requireNonEmptyOrElseGet(
      tableName,
      () -> aClass.getSimpleName().toLowerCase(Locale.ROOT)
    );
  }

  private List<Field> getColumns(final Class<?> aClass) {
    return Arrays.stream(aClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(Column.class))
      .peek(column -> LOGGER.info("Found column '{}'", column.getName()))
      .toList();
  }

  private Field getPrimaryKey(final Class<?> aClass) {
    return Arrays.stream(aClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
      .peek(pk -> LOGGER.info("Found primary key '{}'", pk.getName()))
      .findFirst()
      .orElseThrow();
  }
}
