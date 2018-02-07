package com.virtualbank.account.controller;

import com.virtualbank.account.*;
import com.virtualbank.account.external.WeatherApiService;
import com.virtualbank.account.service.AccountService;
import com.virtualbank.account.service.TransactioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by SANJIT on 13/01/18.
 */



@RestController
@RequestMapping("/api")
public class BankAccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    TransactioService transactioService;

    @Autowired
    WeatherApiService weatherApiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountController.class);


    @PostMapping(value = "/accounts", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        LOGGER.info("Received request for to create new account");
        CreateAccountResponse response =  accountService.createAccount(createAccountRequest);
        return new ResponseEntity<CreateAccountResponse>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/accounts/{id}/balance/transactions", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AccountFundOperationResponse> balanceFundOperation(@PathVariable(value = "id" ) Long accountId, @RequestBody AccountFundOperationRequest accountFundOperationRequest){
        LOGGER.info("Received request for to fund balance operation for {}", accountId );
        AccountFundOperationResponse updatedAccountResponse = accountService.accountFundOperation(accountId, accountFundOperationRequest);
        return ResponseEntity.ok(updatedAccountResponse);
    }


    @PostMapping(value = "/transfer", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<FundTransferResponse> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest){
       LOGGER.info("Received request for fund transfer from account {}", fundTransferRequest.getFromAccount() );
       FundTransferResponse fundTransferResponse = accountService.transferFund(fundTransferRequest);
       return ResponseEntity.ok(fundTransferResponse);
    }

    @GetMapping(value = "/accounts/{id}/transactions",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<TransactionsResponse> getTransactions(@PathVariable(value = "id" ) Long accountId){
        LOGGER.info("Received request to fetch transaction for account {}", accountId );
        LOGGER.debug("Calling weather api : " + weatherApiService.getWeatherByCity().toString());
        TransactionsResponse transactionsResponse = transactioService.getTransaction(accountId);
        return ResponseEntity.ok(transactionsResponse);
    }
}
