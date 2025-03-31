package com.example.toss_pratice.transaction.exception;

public class TransactionNotFound extends RuntimeException{
    public TransactionNotFound(String message) {
        super(message);
    }
}
