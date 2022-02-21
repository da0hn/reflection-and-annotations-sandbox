package org.gabriel.sandbox.rene.orm;

import java.sql.SQLException;

public interface ReneConnection {

  void closeConnection() throws SQLException;

}
