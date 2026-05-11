package com.mat.hexa_finance.domain.exception;

public class RecurringTransactionNotFoundException extends RuntimeException {
    public RecurringTransactionNotFoundException(String message) {

        super(message);
    }
}
