package org.gabriel.sandbox.rene.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;

enum ColumnValueApplier implements ColumnValueTypeApplier {
  INTEGER {
    @Override
    public void apply(final PreparedStatement preparedStatement, final Insert.ColumnValue<?> columnValue) throws SQLException {
      preparedStatement.setInt(columnValue.index(), (int) columnValue.value());
    }
  },
  STRING {
    @Override
    public void apply(final PreparedStatement preparedStatement, final Insert.ColumnValue<?> columnValue) throws SQLException {
      preparedStatement.setString(columnValue.index(), (String) columnValue.value());
    }
  },
  LONG {
    @Override
    public void apply(final PreparedStatement preparedStatement, final Insert.ColumnValue<?> columnValue) throws SQLException {
      preparedStatement.setLong(columnValue.index(), (Long) columnValue.value());
    }
  },
  DOUBLE {
    @Override
    public void apply(final PreparedStatement preparedStatement, final Insert.ColumnValue<?> columnValue) throws SQLException {
      preparedStatement.setDouble(columnValue.index(), (Double) columnValue.value());
    }
  },
  FLOAT {
    @Override
    public void apply(final PreparedStatement preparedStatement, final Insert.ColumnValue<?> columnValue) throws SQLException {
      preparedStatement.setFloat(columnValue.index(), (Float) columnValue.value());
    }
  }
}
