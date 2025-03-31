package com.example.toss_pratice.payment.exception;

public class AlreadyCompleteToPaymentException extends RuntimeException{
    public AlreadyCompleteToPaymentException(String message) {
        super(message);
    }
}
