package org.gabriel.sandbox.rene.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReneOrm<T> implements ReneOperations<T>, ReneConnection {

  static final Map<Class<?>, Value> valueStatementMap = new HashMap<>();
  private static final Logger LOGGER = LoggerFactory.getLogger(ReneOrm.class);

  static {
    valueStatementMap.put(Integer.class, Value.INTEGER);
    valueStatementMap.put(String.class, Value.STRING);
    valueStatementMap.put(Double.class, Value.DOUBLE);
    valueStatementMap.put(Long.class, Value.LONG);
    valueStatementMap.put(Float.class, Value.FLOAT);
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
    final ReneInsertOperation<T> insert = new Insert<>();
    insert.execute(entity, this.connection);
  }

  @Override
  public void closeConnection() throws SQLException {
    LOGGER.info("Closing connection");
    this.connection.close();
  }

  protected enum Value {
    INTEGER {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setInt(columnValue.index(), (int) columnValue.value());
      }
    },
    STRING {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setString(columnValue.index(), (String) columnValue.value());
      }
    },
    LONG {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setLong(columnValue.index(), (Long) columnValue.value());
      }
    },
    DOUBLE {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setDouble(columnValue.index(), (Double) columnValue.value());
      }
    },
    FLOAT {
      @Override void apply(final PreparedStatement preparedStatement, final ColumnValue<?> columnValue) throws SQLException {
        preparedStatement.setFloat(columnValue.index(), (Float) columnValue.value());
      }
    };

    abstract void apply(PreparedStatement preparedStatement, ColumnValue<?> columnValue) throws SQLException;
  }

  record ColumnValue<VALUE>(int index, VALUE value) {}

}
