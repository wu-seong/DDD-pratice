package com.example.toss_pratice.payment;

import com.example.toss_pratice.order.Order;
import com.example.toss_pratice.payment.exception.DifferentAmountException;
import com.example.toss_pratice.payment.exception.ExistActivePaymentException;
import com.example.toss_pratice.payment.exception.PaymentNotFound;
import com.example.toss_pratice.transaction.Amount;
import com.example.toss_pratice.transaction.Transaction;
import com.example.toss_pratice.transaction.TransactionService;
import com.example.toss_pratice.transaction.TransactionType;
import com.example.toss_pratice.transaction.exception.TransactionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private final List<Payment> paymentList;
    private final TransactionService transactionService;

    @Autowired
    public PaymentService(List<Payment> paymentList, TransactionService transactionService) {
        this.paymentList = paymentList;
        this.transactionService = transactionService;
    }

    /*
     * 결제를 승인하는 메서드
     */
    public Payment approvePayment(Order order, int inputPayment){
        if (order.getOrderPrice().amount() != inputPayment){
            throw new DifferentAmountException("결제 금액과 주문 금액이 일치하지 않습니다.");
        }

        // 이전에 해당 주문에 대해 진행중인 결제가 있는지를 확인
        boolean hasActivePayment = this.paymentList
                .stream()
                .anyMatch((p) -> p.getOrder().getId().equals(order.getId()) && p.getStatus().equals(PaymentStatus.INITIATED));
        if (hasActivePayment){
            throw new ExistActivePaymentException("해당 주문에 대해 진행중인 결제가 존재합니다.");
        }
        // 이미 완료된 주문인지를 확인
        boolean alreadyCompleteToPayment = this.paymentList
                .stream()
                .anyMatch((p) -> p.getOrder().getId().equals(order.getId()) && p.getStatus().equals(PaymentStatus.CANCELLED));
        if (alreadyCompleteToPayment){
            throw new ExistActivePaymentException("해당 주문에 대해 이미 완료된 결제가 존재합니다.");
        }

        // 그렇지 않으면 결제를 새로 만듬
        Payment payment = new Payment(UUID.randomUUID(), order, new TotalAmount(order.getOrderPrice().amount()), new ArrayList<>());
        Transaction transaction = new Transaction(UUID.randomUUID(), payment.getId(), TransactionType.PAYMENT, new Amount(payment.getAmount().amount()), LocalDateTime.now());
        payment.addTransaction(transaction);
        transactionService.addTransaction(transaction);
        payment.setCompleted();
        return payment;
    }

    /*
     * 특정 주문에 대한 결제 내역 존재 여부를 묻는 메서드
     */

    public boolean existByOrderId(UUID orderId){
        return paymentList.stream()
                .filter((payment -> payment.getOrder().getId().equals(orderId)))
                .findAny()
                .isPresent();
    }
    /*
     * 특정 주문에 대한 결제 내역을 조회하는 메서드
     */
    public Payment findByOrderId(UUID orderId){
        return paymentList.stream()
                .filter((payment -> payment.getOrder().getId().equals(orderId)))
                .findAny()
                .orElseThrow(() -> new PaymentNotFound("해당 주문에 대한 결제가 존재하지 않습니다."));
    }
    /*
     * 특정 결제에 대한 거래내역을 불러오는 메서드
     */
    public List<Transaction> getTransactions(UUID paymentId){
        Payment payment = paymentList.stream()
                .filter(p -> p.getId().equals(paymentId))
                .findAny()
                .orElseThrow(() ->  new PaymentNotFound("존재하지 않습니다."));
        payment.getTransactionList()
                .stream()
                .forEach(p -> {
                    if (transactionService.isExist(p.getId())){
                       throw new TransactionNotFound("해당 ID의 트랜잭션은 존재하지 않습니다.");
                    }
                });
        return payment.getTransactionList();
    }
}
