package com.example.toss_pratice.transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID paymentId;
    private final TransactionType type;
    private final Amount transactionAmount;
    private final LocalDateTime timestamp;

    public Transaction(UUID id, UUID paymentId, TransactionType type, Amount transactionAmount, LocalDateTime timestamp) {
        this.id = id;
        this.paymentId = paymentId;
        this.type = type;
        this.transactionAmount = transactionAmount;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPaymentId() {
        return paymentId;
    }
}
