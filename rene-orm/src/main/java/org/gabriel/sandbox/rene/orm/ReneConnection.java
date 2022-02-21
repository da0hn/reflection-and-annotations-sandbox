package org.gabriel.sandbox.rene.orm;

import java.sql.SQLException;

interface ReneConnection {

  void closeConnection() throws SQLException;

}
