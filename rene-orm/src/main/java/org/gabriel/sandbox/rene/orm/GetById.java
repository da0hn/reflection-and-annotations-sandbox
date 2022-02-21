package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.orm.extractor.ColumnValueExtractor;
import org.gabriel.sandbox.rene.orm.extractor.ColumnValueTypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

class GetById<T> implements ReneGetByIdOperation<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetById.class);
  private final Connection connection;

  GetById(final Connection connection) {
    this.connection = connection;
  }

  @Override public Optional<T> execute(
    final Class<T> aClass,
    final Long id
  ) {
    try {
      Objects.requireNonNull(aClass);
      Objects.requireNonNull(id);

      final var sql = "SELECT * FROM %s WHERE %s=%d";

      final var primaryKey = Arrays.stream(aClass.getDeclaredFields())
        .filter(f -> f.isAnnotationPresent(PrimaryKey.class))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Primary key not found"));

      final var formattedSql = String.format(
        sql,
        GetById.getTableName(aClass),
        primaryKey.getName(),
        id
      );

      LOGGER.info("Generated query:");
      LOGGER.info(formattedSql);

      final var preparedStatement = this.connection.prepareStatement(formattedSql);

      final var resultSet = preparedStatement.executeQuery();

      if(!resultSet.next()) {
        return Optional.empty();
      }

      final var instance = aClass.getConstructor().newInstance();

      final var transactionId = resultSet.getLong(primaryKey.getName());
      primaryKey.setAccessible(true);
      primaryKey.set(instance, transactionId);

      for(final var field : aClass.getDeclaredFields()) {
        if(field.isAnnotationPresent(Column.class)) {
          field.setAccessible(true);
          final ColumnValueTypeExtractor<?> extractor = ColumnValueExtractor.getExtractor(field.getType());
          final Object value = extractor.extract(resultSet).apply(field);
          field.set(instance, value);
        }
      }

      return Optional.of(instance);
    }
    catch(final SQLException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  private static String getTableName(final Class<?> aClass) {
    return new GetTableName().execute(aClass);
  }
}
