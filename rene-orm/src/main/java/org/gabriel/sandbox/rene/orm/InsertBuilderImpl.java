package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InsertBuilderImpl implements InsertBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(InsertBuilderImpl.class);
  private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s, %s) VALUES (%s)";
  private final Class<?> aClass;
  private final List<Field> columns;

  InsertBuilderImpl(final Class<?> aClass, final List<Field> columns) {
    this.aClass = aClass;
    this.columns = Collections.unmodifiableList(columns);
  }

  @Override public String build() {
    final var joinedColumnName = this.joinColumnNameSeparatedByComma();
    final var columnsPlaceholder = this.getColumnValueAsPlaceholder();
    final var pkColumn = this.getPrimaryKey();
    final String formattedName = this.getTableName();
    return String.format(INSERT_TEMPLATE, formattedName, pkColumn.getName(), joinedColumnName, columnsPlaceholder);
  }

  private String joinColumnNameSeparatedByComma() {
    return this.columns.stream()
      .map(InsertBuilderImpl::getColumnName)
      .collect(Collectors.joining(", "));
  }

  private static String getColumnName(final Field field) {
    final var columnName = Optional.ofNullable(field.getAnnotation(Column.class))
      .map(Column::name)
      .orElseThrow(IllegalStateException::new);
    return StringUtil.requireNonEmptyOrElseGet(
      columnName,
      () -> field.getName().toLowerCase(Locale.ROOT)
    );
  }

  private String getColumnValueAsPlaceholder() {
    return IntStream.range(0, this.getNumberOfColumns() + 1)
      .mapToObj(column -> "?")
      .collect(Collectors.joining(", "));
  }

  private int getNumberOfColumns() {
    return this.columns.size();
  }

  private String getTableName() {
    final var tableName = Optional.ofNullable(this.aClass.getAnnotation(Table.class))
      .map(Table::name)
      .orElseThrow(IllegalStateException::new);

    return StringUtil.requireNonEmptyOrElseGet(
      tableName,
      () -> this.aClass.getSimpleName().toLowerCase(Locale.ROOT)
    );
  }

  private Field getPrimaryKey() {
    final var pkColumn = Arrays.stream(this.aClass.getDeclaredFields())
      .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
      .peek(pk -> LOGGER.info("Found primary key '{}'", pk.getName()))
      .findFirst()
      .orElseThrow();
    if(pkColumn.getType() != Long.class) {
      throw new IllegalStateException();
    }
    return pkColumn;
  }
}
