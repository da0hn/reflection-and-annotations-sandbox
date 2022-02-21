package org.gabriel.sandbox.rene.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReneOrm<T> implements ReneOperations<T>, ReneConnection {

  static final Map<Class<?>, ColumnValueApplier> valueStatementMap = new HashMap<>();
  private static final Logger LOGGER = LoggerFactory.getLogger(ReneOrm.class);

  static {
    valueStatementMap.put(Integer.class, ColumnValueApplier.INTEGER);
    valueStatementMap.put(String.class, ColumnValueApplier.STRING);
    valueStatementMap.put(Double.class, ColumnValueApplier.DOUBLE);
    valueStatementMap.put(Long.class, ColumnValueApplier.LONG);
    valueStatementMap.put(Float.class, ColumnValueApplier.FLOAT);
  }

  private final Connection connection;

  private ReneOrm() throws SQLException {
    LOGGER.info("Opening connection");
    this.connection = DriverManager.getConnection(
      "jdbc:h2:~/external_hd/code/reflection-and-annotations-sandbox/rene-orm/h2_database/any_database",
      "sa",
      ""
    );
  }

  public static <T> ReneOrm<T> create() throws SQLException {
    return new ReneOrm<>();
  }

  @Override
  public void insert(final T entity) throws SQLException, IllegalAccessException {
    LOGGER.info("Begin insertion of {}", entity);
    final ReneInsertOperation<T> insert = new Insert<>(this.connection);
    insert.execute(entity);
    LOGGER.info("End insertion");
  }

  @Override
  public void closeConnection() throws SQLException {
    LOGGER.info("Closing connection");
    this.connection.close();
  }

}
