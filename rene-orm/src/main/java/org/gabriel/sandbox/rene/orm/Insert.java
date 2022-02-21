package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Insert<T> implements ReneInsertOperation<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Insert.class);
  private final AtomicLong idCounter = new AtomicLong(0L);

  @Override
  public void execute(final T entity, final Connection connection) throws SQLException, IllegalAccessException {

    final Class<?> aClass = entity.getClass();

    final var columns = this.getColumns(aClass);

    final var joinedColumnName = this.joinColumnNameSeparatedByComma(columns);

    final var columnsPlaceholder = this.getColumnValueAsPlaceholder(columns.size());

    final var pkColumn = this.getPrimaryKey(aClass);

    final String formattedName = this.getTableName(aClass);

    final String insertStatement = """
                                   INSERT INTO %s (%s, %s) VALUES (%s)
                                   """.formatted(formattedName, pkColumn.getName(), joinedColumnName, columnsPlaceholder);

    final var preparedStatement = connection.prepareStatement(insertStatement);

    this.setId(pkColumn, preparedStatement);

    var index = 2;
    for(final var column : columns) {
      column.setAccessible(true);
      final var statementApplier = ReneOrm.valueStatementMap.get(column.getType());
      statementApplier.apply(preparedStatement, new ReneOrm.ColumnValue<>(index++, column.get(entity)));
    }

    LOGGER.info("Statement created {}", insertStatement);
    final var updateCount = preparedStatement.executeUpdate();
    LOGGER.info("Statement executed, update count: {}", updateCount);
  }

  private String joinColumnNameSeparatedByComma(final Collection<Field> columns) {
    return columns.stream()
      .map(this::getColumnName)
      .collect(Collectors.joining(", "));
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

  private String getColumnValueAsPlaceholder(final int numberOfColumns) {
    return IntStream.range(0, numberOfColumns + 1)
      .mapToObj(column -> "?")
      .collect(Collectors.joining(", "));
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

  private void setId(
    final Field pkColumn,
    final PreparedStatement preparedStatement
  ) {
    try {
      if(pkColumn.getType() != Long.class) {
        throw new IllegalStateException();
      }
      preparedStatement.setLong(1, this.idCounter.getAndIncrement());
    }
    catch(final SQLException | IllegalArgumentException e) {
      LOGGER.error(e.getMessage());
      throw new IllegalStateException();
    }
  }
}
