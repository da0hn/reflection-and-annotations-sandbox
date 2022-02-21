package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

class Insert<T> implements ReneInsertOperation<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(Insert.class);
  private final AtomicLong idCounter = new AtomicLong(0L);

  @Override
  public void execute(final T entity, final Connection connection) throws SQLException, IllegalAccessException {

    final Class<?> aClass = entity.getClass();

    final var columns = Insert.getColumns(aClass);

    final String insertStatement = new InsertBuilderImpl(aClass, columns).build();

    final var preparedStatement = connection.prepareStatement(insertStatement);

    this.applyIdValueInStatement(preparedStatement);

    this.applyColumnValueInStatement(entity, columns, preparedStatement);

    LOGGER.info("Statement created {}", insertStatement);
    final var updateCount = preparedStatement.executeUpdate();
    LOGGER.info("Statement executed, update count: {}", updateCount);
  }

  private void applyColumnValueInStatement(
    final T entity,
    final Iterable<Field> columns,
    final PreparedStatement preparedStatement
  ) throws SQLException, IllegalAccessException {
    var index = 2;
    for(final var column : columns) {
      column.setAccessible(true);
      final var statementApplier = ReneOrm.valueStatementMap.get(column.getType());
      statementApplier.apply(preparedStatement, new ColumnValue<>(index++, column.get(entity)));
    }
  }

  private static List<Field> getColumns(final Class<?> aClass) {
    return Arrays.stream(aClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(Column.class))
      .peek(column -> LOGGER.info("Found column '{}'", column.getName()))
      .toList();
  }

  private void applyIdValueInStatement(
    final PreparedStatement preparedStatement
  ) {
    try {
      preparedStatement.setLong(1, this.idCounter.getAndIncrement());
    }
    catch(final SQLException | IllegalArgumentException e) {
      LOGGER.error(e.getMessage());
      throw new IllegalStateException();
    }
  }

  record ColumnValue<VALUE>(int index, VALUE value) {}
}
