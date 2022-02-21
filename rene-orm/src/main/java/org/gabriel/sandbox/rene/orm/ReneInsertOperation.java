package org.gabriel.sandbox.rene.orm;

import java.sql.Connection;
import java.sql.SQLException;

interface ReneInsertOperation<T> {
  void execute(final T entity, final Connection connection) throws SQLException, IllegalAccessException;
}
