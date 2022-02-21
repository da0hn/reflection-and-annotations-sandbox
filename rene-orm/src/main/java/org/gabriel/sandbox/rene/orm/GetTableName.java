package org.gabriel.sandbox.rene.orm;

import org.gabriel.sandbox.rene.annotations.Table;

import java.util.Locale;
import java.util.Optional;

public class GetTableName {

  public String execute(final Class<?> aClass) {
    final var tableName = Optional.ofNullable(aClass.getAnnotation(Table.class))
      .map(Table::name)
      .orElseThrow(IllegalStateException::new);

    return StringUtil.requireNonEmptyOrElseGet(
      tableName,
      () -> aClass.getSimpleName().toLowerCase(Locale.ROOT)
    );
  }

}
