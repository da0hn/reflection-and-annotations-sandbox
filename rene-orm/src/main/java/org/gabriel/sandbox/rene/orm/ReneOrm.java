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
import java.util.stream.IntStream;

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

    final String formattedName = this.getTableName(aClass);

    final var joinedColumnName = columns.stream()
      .map(this::getColumnName)
      .collect(Collectors.joining(", "));

    //    final var joinedColumnValue = columns.stream()
    //      .map(field -> this.getColumnValue(field, entity))
    //      .collect(Collectors.joining(", "));

    final var columnsPlaceholder = IntStream.range(0, columns.size() + 1)
      .mapToObj(column -> "?")
      .collect(Collectors.joining(", "));

    final var pkColumn = this.getPrimaryKey(aClass);

    final String insertStatement = """
                                   INSERT INTO %s (%s, %s) VALUES (%s)
                                   """.formatted(formattedName, pkColumn.getName(), joinedColumnName, columnsPlaceholder);

    LOGGER.info(insertStatement);

  }

  private String getColumnValue(final Field field, final T entity) {
    try {
      field.setAccessible(true);
      final var value = field.get(entity);
      final Class<?> type = field.getType();

      return switch(type.getSimpleName()) {
        case "String" -> String.format("'%s'", value.toString());
        case "Integer", "Long", "Float", "Double" -> value.toString();
        default -> throw new IllegalStateException();
      };
    }
    catch(final IllegalAccessException e) {
      LOGGER.error(e.getMessage());
      throw new IllegalStateException();
    }
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
