package org.gabriel.sandbox.rene;

import org.gabriel.sandbox.rene.model.TransactionHistory;
import org.gabriel.sandbox.rene.orm.ReneOrm;

import java.sql.SQLException;

public class Application {

  public static void main(final String... args) throws SQLException, IllegalAccessException {

    final var transaction1 = new TransactionHistory(15331, "Gabriel 1", "Credit", 1_0000d);
    final var transaction2 = new TransactionHistory(15342, "Gabriel 2", "Debit", 500.00);
    final var transaction4 = new TransactionHistory(15353, "Gabriel 4", "Debit", 275.50);
    final var transaction3 = new TransactionHistory(15364, "Gabriel 3", "Credit", 125_000.00);

    final ReneOrm<TransactionHistory> reneOrm = ReneOrm.create();

    reneOrm.insert(transaction1);
    reneOrm.insert(transaction2);
    reneOrm.insert(transaction3);
    reneOrm.insert(transaction4);

    reneOrm.closeConnection();
  }

}
