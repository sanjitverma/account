package com.virtualbank.account.service;

import com.virtualbank.account.*;
import com.virtualbank.account.constants.AccountType;
import com.virtualbank.account.constants.BalanceOperation;
import com.virtualbank.account.constants.TransactionType;
import com.virtualbank.account.entity.Account;
import com.virtualbank.account.entity.Transaction;
import com.virtualbank.account.exception.business.AccountNotFoundException;
import com.virtualbank.account.exception.business.InSufficientBalanceException;
import com.virtualbank.account.repository.AccountRepository;
import com.virtualbank.account.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SANJIT on 13/01/18.
 */
@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        Account account = this.createAccountBO(createAccountRequest);
        Account accountCreated = accountRepository.save(account);
        CreateAccountResponse createAccountResponse = new CreateAccountResponse();
        createAccountResponse.setId(String.valueOf(accountCreated.getId()));
        createAccountResponse.setStatus("SUCCESS");
        LOGGER.info("Account id {} successfully created", accountCreated.getId());
        return createAccountResponse;
    }

    private Account createAccountBO(CreateAccountRequest createAccountRequest) {
        Date dob = null;
        Account account = new Account();
        account.setFirstName(createAccountRequest.getFirstName());
        account.setLastName(createAccountRequest.getLastName());
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(createAccountRequest.getDOB().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        account.setDateOfBirth(dob);
        account.setAccountType(AccountType.valueOf(createAccountRequest.getAccountType().toString()));
        account.setAccountBalance(0.0f);
        return account;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountFundOperationResponse accountFundOperation(Long accountId, AccountFundOperationRequest accountFundOperationRequest) {

        LOGGER.info("Account fund operation received for {}", accountId);

        Account account = accountRepository.findOne(accountId);
        this.validateAccount(account,accountId);

        Float requestAmount = Float.valueOf(accountFundOperationRequest.getAmount());
        Float existingAmount = account.getAccountBalance();
        AccountFundOperationRequest.FundActionType fundActionType = accountFundOperationRequest.getFundActionType();
        AccountFundOperationResponse fundOperationResponse = new AccountFundOperationResponse();
        Float remainingBalance = 0.00f;
        BalanceOperation balanceOperation = null;
        TransactionType transactionType = null;

        this.validateFund(account, accountFundOperationRequest);

        if (fundActionType.toString().equals("WITHDRAW")) {
            remainingBalance = existingAmount - requestAmount;
            account.setAccountBalance(remainingBalance);
            balanceOperation = BalanceOperation.WITHDRAW_FUND;
            transactionType = TransactionType.DEBIT;
        } else if (fundActionType.toString().equals("DEPOSIT")) {
            remainingBalance = existingAmount + requestAmount;
            account.setAccountBalance(remainingBalance);
            balanceOperation = BalanceOperation.ADD_FUND;
            transactionType = TransactionType.CREDIT;
        }

        Transaction transaction = this.createTransaction(requestAmount, balanceOperation, accountId, transactionType);
        accountRepository.save(account);
        transactionRepository.save(transaction);
        fundOperationResponse.setStatus("SUCCESS");
        fundOperationResponse.setRemainingBalance(Float.toString(remainingBalance));
        return fundOperationResponse;
    }

    private Transaction createTransaction(Float requestAmount, BalanceOperation balanceOperation, Long accountId, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setDebitAccount(accountId);
        transaction.setAmount(requestAmount);
        transaction.setTransactionDate(new Date());
        transaction.setBalanceOperation(balanceOperation);
        transaction.setTransactionType(transactionType);
        return transaction;
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FundTransferResponse transferFund(FundTransferRequest fundTransferRequest) {


        LOGGER.info("Fund transfer from {} to {} account request received ", fundTransferRequest.getFromAccount(),
                fundTransferRequest.getToAccount());

        FundTransferResponse fundTransferResponse = new FundTransferResponse();

        Long fromAccountId = Long.valueOf(fundTransferRequest.getFromAccount());
        Long toAccountId = Long.valueOf(fundTransferRequest.getToAccount());
        Float transferAmount = Float.parseFloat(fundTransferRequest.getAmount());
        Float accountBalanceInCreditAccount = null;
        Float accountBalanceInDebitAccount = null;

        //Check for validity of both the account
        Account fromAccount = accountRepository.findOne(fromAccountId);
        this.validateAccount(fromAccount, fromAccountId);
        Account toAccount = accountRepository.findOne(toAccountId);
        this.validateAccount(toAccount, toAccountId);

        //Check for sufficient fund
        if(this.validateAccountBalance(fromAccount,transferAmount)){
            throw new InSufficientBalanceException(Account.class, "remaining balance", String.valueOf(fromAccount.getAccountBalance()));
        }

        //Perform fund transfer operation
        accountBalanceInDebitAccount = fromAccount.getAccountBalance() - transferAmount;
        accountBalanceInCreditAccount = toAccount.getAccountBalance() + transferAmount;

        fromAccount.setAccountBalance(accountBalanceInDebitAccount);
        toAccount.setAccountBalance(accountBalanceInCreditAccount);

        //Create the transaction to persist
        Transaction transaction = this.createTransaction(transferAmount, BalanceOperation.FUND_TRANSFER, fromAccountId, TransactionType.DEBIT);
        transaction.setCreditAccount(toAccountId);

        //Save accounts and transaction
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        fundTransferResponse.setStatus("SUCCESS");
        fundTransferResponse.setRemainingBalance(String.valueOf(accountBalanceInDebitAccount));

        LOGGER.info("Fund transfer from {} to {} account completed successfully ", fundTransferRequest.getFromAccount(),
        fundTransferRequest.getToAccount());
        return fundTransferResponse;

    }

    private boolean validateAccountBalance(Account fromAccount, Float transferAmount) {
        float accountBalance = fromAccount.getAccountBalance();
        return transferAmount.compareTo(accountBalance) >= 0 ? true : false;
    }

    private void validateAccount(Account account, Long accountId){
        if (account == null) {
            throw new AccountNotFoundException(Account.class, "accountId", accountId.toString());
        }
    }

    private void validateFund(Account account, AccountFundOperationRequest accountFundOperationRequest){
        AccountFundOperationRequest.FundActionType fundActionType = accountFundOperationRequest.getFundActionType();
        float requestAmount = Float.parseFloat(accountFundOperationRequest.getAmount());
        float existingAmount = account.getAccountBalance();
        if (fundActionType.toString().equals("WITHDRAW") && requestAmount > existingAmount) {
            throw new InSufficientBalanceException(Account.class, "remaining amount", String.valueOf(existingAmount));
        }
    }
}
