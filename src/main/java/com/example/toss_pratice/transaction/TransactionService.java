package com.example.toss_pratice.transaction;

import com.example.toss_pratice.transaction.exception.TransactionNotFound;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final List<Transaction> transactionList;

    public TransactionService(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void addTransaction(Transaction transaction){
        this.transactionList.add(transaction);
    }

    /*
     * ID를 통해 트랜잭션을 제대로 가져올 수 있는지를 테스트
     */
    public Transaction findTransactionById(UUID transactionId){
        Transaction transaction = transactionList.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findAny()
                .orElseThrow(() -> new TransactionNotFound("해당 트랜잭션은 존재하지 않습니다."));
        return transaction;
    }

    /*
     * 해당 거래내역이 실제로 존재하는지를 확인하는 메서드
     */
    public Boolean isExist(UUID transactionId){
        return transactionList.stream()
                .anyMatch(t -> t.getId().equals(transactionId));
    }
}
