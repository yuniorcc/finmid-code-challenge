package com.yunior.bank.persistance.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.yunior.bank.domain.transaction.model.Transaction;
import com.yunior.bank.domain.transaction.model.TransactionResult;
import com.yunior.bank.persistance.account.entity.AccountEntity;
import com.yunior.bank.persistance.account.repository.AccountRepository;
import com.yunior.bank.persistance.transaction.entity.TransactionEntity;
import com.yunior.bank.persistance.transaction.repository.TransactionRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseTransactionPersistenceTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private DatabaseTransactionPersistence persistence;

    @BeforeAll
    void setup() {

        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        persistence = new DatabaseTransactionPersistence(accountRepository, transactionRepository);
    }

    @BeforeEach
    void cleanUp() {

        clearInvocations(accountRepository);
        clearInvocations(transactionRepository);
    }

    @Test
    void shouldCreateTransactionWhenAccountsFound() {

        Transaction transaction = new Transaction(new BigDecimal(981.83), RandomStringUtils.random(8), RandomStringUtils.random(8));

        BigDecimal sourceBalance = new BigDecimal(8731.01);
        BigDecimal destinationBalance = new BigDecimal(10551.99);

        AccountEntity source = new AccountEntity(sourceBalance);
        AccountEntity destination = new AccountEntity(destinationBalance);

        doReturn(Optional.of(source)).when(accountRepository).findById(transaction.source());
        doReturn(Optional.of(destination)).when(accountRepository).findById(transaction.destination());

        TransactionEntity expectedTransactionToBeSaved = new TransactionEntity(transaction.amount(), source.getAccountId(), destination.getAccountId());

        Optional<TransactionResult> result = persistence.createTransaction(transaction);

        verify(accountRepository).save(source);
        verify(accountRepository).save(destination);
        verify(transactionRepository).save(expectedTransactionToBeSaved);
        assertEquals(sourceBalance.subtract(transaction.amount()), result.get().sourceAccountBalance());
        assertEquals(destinationBalance.add(transaction.amount()), result.get().destinationAccountBalance());
    }

    @ParameterizedTest
    @MethodSource("provideAccounts")
    void shouldReturnEmptyOptionalWhenAnyAccountNotFound(AccountEntity source, AccountEntity destination) {

        Transaction transaction = new Transaction(new BigDecimal(100), RandomStringUtils.random(8), RandomStringUtils.random(8));

        doReturn(Optional.ofNullable(source)).when(accountRepository).findById(transaction.source());
        doReturn(Optional.ofNullable(destination)).when(accountRepository).findById(transaction.destination());

        Optional<TransactionResult> result = persistence.createTransaction(transaction);

        verify(accountRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
        assertEquals(Optional.empty(), result);
    }

    private static Stream<Arguments> provideAccounts() {
        return Stream.of(
            Arguments.of(null, new AccountEntity(new BigDecimal(10))),
            Arguments.of(new AccountEntity(new BigDecimal(10)), null),
            Arguments.of(null, null)
        );
    }
}
