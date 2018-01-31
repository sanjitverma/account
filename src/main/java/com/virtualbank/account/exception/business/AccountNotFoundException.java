package com.virtualbank.account.exception.business;

import com.virtualbank.account.exception.util.ExceptionUtil;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by SANJIT on 13/01/18.
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Class cl, String... searchParamsMap) {
        super(AccountNotFoundException.generateMessage(cl.getSimpleName(), ExceptionUtil.toMap(String.class, String.class, searchParamsMap)));

    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " was not found for parameters " +
                searchParams;
    }

}
