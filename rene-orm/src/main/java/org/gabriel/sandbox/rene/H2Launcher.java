package org.gabriel.sandbox.rene;

import org.h2.tools.Server;

import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Launcher {
  public static void main(final String[] args) throws SQLException, ClassNotFoundException {
    //createDatabase();
    Server.main("-ifNotExists", "-web");
  }

  private static void createDatabase() throws ClassNotFoundException, SQLException {
    Class.forName("org.h2.Driver");
    final var connection = DriverManager.getConnection(
      "jdbc:h2:~/external_hd/code/reflection-and-annotations-sandbox/rene-orm/h2_database/any_database",
      "sa",
      ""
    );
    System.out.println("Database created!");
  }
}
