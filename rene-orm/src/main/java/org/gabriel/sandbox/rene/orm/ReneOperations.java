package org.gabriel.sandbox.rene.orm;

import java.sql.SQLException;

public interface ReneOperations<T> {

  void insert(final T entity) throws SQLException, IllegalAccessException;
}
