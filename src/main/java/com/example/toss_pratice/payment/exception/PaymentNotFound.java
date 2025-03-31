package com.example.toss_pratice.payment.exception;

public class PaymentNotFound extends RuntimeException{
    public PaymentNotFound(String message) {
        super(message);
    }
}
