package com.virtualbank.account.exception.handler;

import com.virtualbank.account.exception.business.AccountNotFoundException;
import com.virtualbank.account.exception.business.InSufficientBalanceException;
import com.virtualbank.account.exception.error.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by SANJIT on 13/01/18.
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AccountExceptionHandler extends ResponseEntityExceptionHandler{

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    protected ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex) {
        String error = "Account not found";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, error, ex));
    }

    @ExceptionHandler(InSufficientBalanceException.class)
    protected ResponseEntity<Object> handleInSufficientBalance(InSufficientBalanceException ex) {
        String error = "Insufficient balance";
        return buildResponseEntity(new ApiError(HttpStatus.NOT_ACCEPTABLE, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


}
