package org.gabriel.sandbox.rene.orm;

import java.sql.SQLException;

interface ReneInsertOperation<T> {
  void execute(final T entity) throws SQLException, IllegalAccessException;
}
