package org.gabriel.sandbox.rene.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;

interface ColumnValueTypeApplier {
  void apply(PreparedStatement preparedStatement, Insert.ColumnValue<?> columnValue) throws SQLException;
}
