package com.mat.hexa_finance.domain.exception;

public class BadUsernameException extends RuntimeException {
    public BadUsernameException(String message) {
        super(message);
    }
}
