package com.example.toss_pratice.payment.exception;

public class DifferentAmountException extends RuntimeException{
    public DifferentAmountException(String message) {
        super(message);
    }
}
