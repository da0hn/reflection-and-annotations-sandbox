package org.gabriel.sandbox.rene.model;

import org.gabriel.sandbox.rene.annotations.Column;
import org.gabriel.sandbox.rene.annotations.PrimaryKey;
import org.gabriel.sandbox.rene.annotations.Table;

@Table(name = "transaction_history")
public class TransactionHistory {

  @PrimaryKey
  private Long transactionId;
  @Column
  private Integer accountNumber;
  @Column
  private String name;
  @Column
  private String transactionType;
  @Column
  private Double amount;

  public TransactionHistory() {
  }

  public TransactionHistory(final Integer accountNumber, final String name, final String transactionType, final Double amount) {
    this.accountNumber = accountNumber;
    this.name = name;
    this.transactionType = transactionType;
    this.amount = amount;
  }

  public Long getTransactionId() {
    return this.transactionId;
  }

  public void setTransactionId(final Long transactionId) {
    this.transactionId = transactionId;
  }

  public Integer getAccountNumber() {
    return this.accountNumber;
  }

  public void setAccountNumber(final Integer accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getTransactionType() {
    return this.transactionType;
  }

  public void setTransactionType(final String transactionType) {
    this.transactionType = transactionType;
  }

  public Double getAmount() {
    return this.amount;
  }

  public void setAmount(final Double amount) {
    this.amount = amount;
  }
}
