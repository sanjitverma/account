package com.virtualbank.account.exception.business;

import com.virtualbank.account.exception.util.ExceptionUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by SANJIT on 13/01/18.
 */
public class InSufficientBalanceException extends RuntimeException {

    public InSufficientBalanceException(Class cl, String... searchParamsMap) {
        super(InSufficientBalanceException.generateMessage(cl.getSimpleName(), ExceptionUtil.toMap(String.class, String.class, searchParamsMap)));
    }


    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) +
                " insufficient fund found " +
                searchParams;
    }
}
