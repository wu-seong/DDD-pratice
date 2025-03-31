package com.example.toss_pratice.payment.exception;

public class ExistActivePaymentException extends RuntimeException{
    public ExistActivePaymentException(String message) {
        super(message);
    }
}
