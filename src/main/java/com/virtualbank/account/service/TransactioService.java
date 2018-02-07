package com.virtualbank.account.service;

import com.virtualbank.account.ResponseTransaction;
import com.virtualbank.account.TransactionsResponse;
import com.virtualbank.account.entity.Account;
import com.virtualbank.account.entity.Transaction;
import com.virtualbank.account.exception.business.AccountNotFoundException;
import com.virtualbank.account.repository.AccountRepository;
import com.virtualbank.account.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by SANJIT on 13/01/18.
 */

@Service
public class TransactioService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactioService.class);

    public TransactionsResponse getTransaction(long accountId) {

        LOGGER.debug("Fetching transaction details for account {}", accountId);
        Account account = accountRepository.findOne(accountId);
        this.validateAccount(account, accountId);
        Optional<List<Transaction>> transactionByDebitAccount = transactionRepository.findTransactionByDebitAccount(accountId);
        LOGGER.info("Fetched {} transactions for account {}", transactionByDebitAccount.get().size(), accountId);
        TransactionsResponse transactionsResponse = transformToResponseTransactionList(transactionByDebitAccount);
        return transactionsResponse;
    }

    private TransactionsResponse transformToResponseTransactionList(Optional<List<Transaction>> transactionByDebitAccount) {

        TransactionsResponse transactionsResponse = new TransactionsResponse();
        List<Transaction> transactions = null;
        List<ResponseTransaction> responseTransactionList = new ArrayList<>();
        if (transactionByDebitAccount.isPresent()) {
            transactions = transactionByDebitAccount.get();
            for (Transaction transaction : transactions) {
                ResponseTransaction responseTransaction = new ResponseTransaction();
                responseTransaction.setTransactionType(ResponseTransaction.TransactionType.valueOf(transaction.getTransactionType().toString()));
                responseTransaction.setAccountOperation(ResponseTransaction.AccountOperation.valueOf(transaction.getBalanceOperation().toString()));
                responseTransaction.setCerditAccount(String.valueOf(transaction.getCreditAccount()));
                responseTransaction.setDebitAccount(String.valueOf(transaction.getDebitAccount()));
                responseTransaction.setTransactionDate(transaction.getTransactionDate().toString());
                responseTransaction.setTransactionId(String.valueOf(transaction.getTransaction_id()));
                responseTransaction.setTransactionAmount(String.valueOf(transaction.getAmount()));
                responseTransactionList.add(responseTransaction);
            }
            transactionsResponse.setResponseTransaction(responseTransactionList);
            transactionsResponse.setMessage("Total " + transactions.size() + " records found");

        } else {
            transactionsResponse.setResponseTransaction(Collections.emptyList());
            transactionsResponse.setMessage("No transaction found for given account");
        }
        return transactionsResponse;
    }

    private void validateAccount(Account account,Long accountId) {
        if (account == null) {
            throw new AccountNotFoundException(Account.class, "accountId", accountId.toString());
        }
    }
}
