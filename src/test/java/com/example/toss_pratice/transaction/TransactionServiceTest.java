package com.example.toss_pratice.transaction;

import com.example.toss_pratice.transaction.exception.TransactionNotFound;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


class TransactionServiceTest {
    TransactionService transactionService;
    @BeforeEach
    public void setup(){
        List<Transaction> transactionList = new ArrayList<>();
         transactionService = new TransactionService(transactionList);
    }
    @Test
    public void 존재하는_트랜잭션을_가져온다 (){

        // given: transaction을 하나 생성하고 저장한다.
        UUID paymentId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(transactionId, paymentId, TransactionType.PAYMENT, new Amount(1000), LocalDateTime.now());
        transactionService.addTransaction(transaction);

        // when: findById를 해서 transaction을 가져온다.
        Transaction transactionById = transactionService.findTransactionById(transactionId);

        // then: 이전에 생성해서 저장한 transactionId와 지금 가져온 transaction이 일치해야한다.
        Assertions.assertThat(transactionById.getPaymentId()).isEqualTo(paymentId);
    }

    @Test
    public void 존재하지_않는_트랜잭션을_가져오면_예외가_발생한다(){
        List<Transaction> transactionList = new ArrayList<>();
        TransactionService transactionService = new TransactionService(transactionList);

        // given: transaction을 하나 생성하고 저장한다.
        UUID paymentId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction(transactionId, paymentId, TransactionType.PAYMENT, new Amount(1000), LocalDateTime.now());
        transactionList.add(transaction);

        // when: 등록되지 않은 ID로 findById를 해서 transaction을 가져온다.
        // then: NotFoundTransaction 예외가 발생한다.
        UUID randomUUID = UUID.randomUUID();
        Assertions.assertThatThrownBy( () -> transactionService.findTransactionById(randomUUID))
                .isInstanceOf(TransactionNotFound.class);
    }
}