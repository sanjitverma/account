package com.virtualbank.account.entity;

import com.virtualbank.account.constants.BalanceOperation;
import com.virtualbank.account.constants.TransactionType;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by SANJIT on 13/01/18.
 */

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transaction_id;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Basic
    private double amount;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Enumerated(EnumType.STRING)
    private BalanceOperation balanceOperation;

    @Basic
    private long debitAccount;

    @Basic
    private long creditAccount;


    public long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BalanceOperation getBalanceOperation() {
        return balanceOperation;
    }

    public void setBalanceOperation(BalanceOperation balanceOperation) {
        this.balanceOperation = balanceOperation;
    }

    public long getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(long debitAccount) {
        this.debitAccount = debitAccount;
    }

    public long getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(long creditAccount) {
        this.creditAccount = creditAccount;
    }
}
