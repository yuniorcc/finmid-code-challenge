package com.yunior.bank.domain.transaction;

import com.yunior.bank.domain.transaction.model.Transaction;
import com.yunior.bank.domain.transaction.model.TransactionResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TransactionHandler {

    private final TransactionPersistence transactionPersistence;

    public Optional<TransactionResult> createTransaction(BigDecimal amount, String sourceAccountId, String destinationAccountId) {

        Transaction transaction = new Transaction(amount, sourceAccountId, destinationAccountId);
        return transactionPersistence.createTransaction(transaction);
    }
}
