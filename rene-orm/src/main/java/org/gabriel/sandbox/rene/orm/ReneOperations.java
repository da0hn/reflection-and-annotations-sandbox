package org.gabriel.sandbox.rene.orm;

import java.sql.SQLException;
import java.util.Optional;

interface ReneOperations<T> {
  void insert(final T entity) throws SQLException, IllegalAccessException;

  Optional<T> getById(final Long id, final Class<T> aClass);
}
