package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReneOrm<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReneOrm.class);
  private static final Map<Class<?>, Value> valueStatementMap = new HashMap<>();

  static {
    valueStatementMap.put(Integer.class, Value.INTEGER);
    valueStatementMap.put(String.class, Value.STRING);
    valueStatementMap.put(Double.class, Value.DOUBLE);
    valueStatementMap.put(Long.class, Value.LONG);
    valueStatementMap.put(Float.class, Value.FLOAT);
  }

  private final Connection connection;
  private final AtomicLong idCounter = new AtomicLong(0L);

  private ReneOrm() throws SQLException {
    LOGGER.info("Opening connection");
    this.connection = DriverManager.getConnection(
      "jdbc:h2:~/external_hd/code/reflection-and-annotations-sandbox/rene-orm/h2_database/any_database",
      "sa",
      ""
    );
  }

  public static <T> ReneOrm<T> getConnection() throws SQLException {
    return new ReneOrm<>();
  }

  private static <VALUE> void setValueInStatement(final BiConsumer<Integer, VALUE> valueSetter, final int index, final VALUE value) {
    valueSetter.accept(index, value);
  }

  public void write(final T entity) throws SQLException, IllegalAccessException {

    final Class<?> aClass = entity.getClass();

    final var columns = this.getColumns(aClass);

    final var joinedColumnName = this.joinColumnNameSeparatedByComma(columns);

    final var columnsPlaceholder = this.getColumnValueAsPlaceholder(columns.size());

    final var pkColumn = this.getPrimaryKey(aClass);

    final String formattedName = this.getTableName(aClass);

    final String insertStatement = """
                                   INSERT INTO %s (%s, %s) VALUES (%s)
                                   """.formatted(formattedName, pkColumn.getName(), joinedColumnName, columnsPlaceholder);

    final var preparedStatement = this.connection.prepareStatement(insertStatement);

    this.setId(pkColumn, preparedStatement);

    var index = 2;
    for(final var column : columns) {
      column.setAccessible(true);
      final var statementApplier = valueStatementMap.get(column.getType());
      statementApplier.apply(preparedStatement, new ColumnValue<>(index++, column.get(entity)));
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

  public void closeConnection() throws SQLException {
    LOGGER.info("Closing connection");
    this.connection.close();
  }

  private enum Value {
    INTEGER {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setInt(columnValue.index(), (int) columnValue.value());
      }
    },
    STRING {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setString(columnValue.index(), (String) columnValue.value());
      }
    },
    LONG {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setLong(columnValue.index(), (Long) columnValue.value());
      }
    },
    DOUBLE {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setDouble(columnValue.index(), (Double) columnValue.value());
      }
    },
    FLOAT {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setFloat(columnValue.index(), (Float) columnValue.value());
      }
    };

    abstract void apply(PreparedStatement preparedStatement, ColumnValue<?> columnValue) throws SQLException;
  }

  private record ColumnValue<VALUE>(int index, VALUE value) {}

}
