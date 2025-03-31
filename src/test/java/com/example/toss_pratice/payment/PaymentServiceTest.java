package com.example.toss_pratice.payment;

import com.example.toss_pratice.order.ItemName;
import com.example.toss_pratice.order.Order;
import com.example.toss_pratice.order.Price;
import com.example.toss_pratice.payment.exception.DifferentAmountException;
import com.example.toss_pratice.transaction.Transaction;
import com.example.toss_pratice.transaction.TransactionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;


class PaymentServiceTest {

    TransactionService transactionService;
    PaymentService paymentService;

    /*
    결제 승인 테스트
        case1: 금액이 일치하여 결제가 성공하는경우
            1-1: 성공
            1-2: 결제 상태가 초기가 아닌 경우 -> 이미 승인된 되거나 취소된 결제에 대해 결제하지 않음
        case2: 금액이 일치하지 않는경우
     */
    @BeforeEach
    void setup(){
        List<Payment> paymentList = new ArrayList<>();
        paymentService = new PaymentService(paymentList, transactionService);
    }
    @Test
    void 주문금액이_다르면_예외를_발생시킨다() {
        // given: 주문을 위해 결제를 진행한다.
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), new ItemName("A"), new Price(1000));
        // when: 이 때 상품금액과 결제금액이 다르다.
        // then: 예외를 발생시킨다.
        Assertions.assertThatThrownBy(() -> paymentService.approvePayment(order, 1100))
                .isInstanceOf(DifferentAmountException.class);
    }
    @Test
    void 주문금액은_같지만_이미_처리된_결제이거나_취소된결제이면_예외를_발생시킨다() {
        // given: 이전에 결제를 하다가 취소된 전적이 있다.
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), new ItemName("A"), new Price(1000));
        // when: 이 때 상품금액과 결제금액은 같지만 결제 상태가 올바르지 않다.
        // then: 예외를 발생시킨다.
        Assertions.assertThatThrownBy(() -> paymentService.approvePayment(order, 1100))
                .isInstanceOf(DifferentAmountException.class);
    }

    @Test
    void 주문금액이_일치하고_결제_준비상태이면_결제를_완료하고_거내래역을_남긴다(){
        // given: 주문한 상품에 대한 결제가 없었를 때
        Order order = new Order(UUID.randomUUID(), UUID.randomUUID(), new ItemName("A"), new Price(1000));
        // when: 결제를 진행한다.
        Payment payment = paymentService.approvePayment(order, 1000);
        // then: 결제 상태가 완료로 바뀌고 거래내역이 남는다.
        Assertions.assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        verify(transactionService).addTransaction(any(Transaction.class));

    }
    @Test
    void getTransactions() {
    }
}