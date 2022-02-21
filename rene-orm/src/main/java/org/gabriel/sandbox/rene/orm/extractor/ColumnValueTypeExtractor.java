package org.gabriel.sandbox.rene.orm.extractor;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public interface ColumnValueTypeExtractor<T> {
  Function<Field, T> extract(final ResultSet resultSet) throws SQLException;
}
