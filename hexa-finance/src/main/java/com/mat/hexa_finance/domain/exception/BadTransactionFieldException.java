package com.mat.hexa_finance.domain.exception;

public class BadTransactionFieldException extends RuntimeException{
    public  BadTransactionFieldException(String message){
        super(message);
    }
}
