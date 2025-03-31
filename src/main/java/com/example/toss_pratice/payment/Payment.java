package com.example.toss_pratice.payment;

import com.example.toss_pratice.order.Order;
import com.example.toss_pratice.payment.exception.PaymentStatusException;
import com.example.toss_pratice.transaction.Transaction;

import java.util.List;
import java.util.UUID;

public class Payment {
    private final UUID id;
    private final Order order;

    public Payment(UUID id, Order order, TotalAmount amount, List<Transaction> transactionList) {
        this.id = id;
        this.order = order;
        this.amount = amount;
        this.transactionList = transactionList;
        this.status = PaymentStatus.INITIATED;
    }

    private final TotalAmount amount;
    private PaymentStatus status;
    private final List<Transaction> transactionList;

    public void addTransaction(Transaction transaction){
        this.transactionList.add(transaction);
    }

    public UUID getId() {
        return id;
    }

    public TotalAmount getAmount() {
        return amount;
    }

    public void setCompleted(){
        if (this.status != PaymentStatus.INITIATED){
            throw new PaymentStatusException("결제 대기 상태에서만 완료할 수 있습니다.");
        }
        this.status = PaymentStatus.COMPLETED;
    }

    public void setCancelled(){
        if (this.status != PaymentStatus.COMPLETED){
            throw new PaymentStatusException("결제 완료 상태에서만 취소할 수 있습니다.");
        }
        this.status = PaymentStatus.CANCELLED;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public Order getOrder() {
        return order;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}
