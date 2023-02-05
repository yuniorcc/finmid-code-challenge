package com.yunior.bank.domain.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.yunior.bank.domain.transaction.model.Transaction;
import com.yunior.bank.domain.transaction.model.TransactionResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionHandlerTest {

    private TransactionHandler transactionHandler;
    private TransactionPersistence transactionPersistence;

    @BeforeAll
    void setup() {

        transactionPersistence = mock(TransactionPersistence.class);
        transactionHandler = new TransactionHandler(transactionPersistence);
    }

    @Test
    void createTransaction_shouldCallTransactionPersistence() {

        BigDecimal amount = new BigDecimal(931.24);
        String sourceAccountId = RandomStringUtils.random(8);
        String destinationAccountId = RandomStringUtils.random(8);

        Transaction expectedTransactionToBeCreated = new Transaction(amount, sourceAccountId, destinationAccountId);
        TransactionResult expectedTransactionResult = new TransactionResult(new BigDecimal(676.14), new BigDecimal(856.99));

        doReturn(Optional.of(expectedTransactionResult)).when(transactionPersistence).createTransaction(expectedTransactionToBeCreated);

        Optional<TransactionResult> result = transactionHandler.createTransaction(amount, sourceAccountId, destinationAccountId);

        assertEquals(expectedTransactionResult, result.get());
    }
}
