package com.yunior.bank.persistance.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.yunior.bank.domain.account.model.Account;
import com.yunior.bank.persistance.account.entity.AccountEntity;
import com.yunior.bank.persistance.account.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DatabaseAccountPersistenceTest {

    private AccountRepository accountRepository;
    private DatabaseAccountPersistence persistence;

    @BeforeAll
    void setup() {

        accountRepository = mock(AccountRepository.class);
        persistence = new DatabaseAccountPersistence(accountRepository);
    }

    @Test
    void shouldCreateAccountWithSpecifiedBalance() {

        BigDecimal balance = new BigDecimal(2564);
        AccountEntity expectedAccountToBeSaved = new AccountEntity(balance);
        AccountEntity expectedAccountSaved = new AccountEntity(RandomStringUtils.random(8), balance);

        doReturn(expectedAccountSaved).when(accountRepository).save(expectedAccountToBeSaved);

        Optional<Account> account = persistence.createAccount(balance);

        assertEquals(expectedAccountSaved.getAccountId(), account.get().accountId());
        assertEquals(expectedAccountSaved.getBalance(), account.get().balance());
    }

    @Test
    void findAccount_shouldReturnAccountOptionalWhenIsFound() {

        String accountId = RandomStringUtils.random(8);
        AccountEntity expectedAccountFound = new AccountEntity(accountId, new BigDecimal(3671));

        doReturn(Optional.of(expectedAccountFound)).when(accountRepository).findById(accountId);

        Optional<Account> account = persistence.findAccount(accountId);

        assertEquals(expectedAccountFound.getAccountId(), account.get().accountId());
        assertEquals(expectedAccountFound.getBalance(), account.get().balance());
    }

    @Test
    void findAccount_shouldReturnEmptyOptionalWhenIsNotFound() {

        String accountId = RandomStringUtils.random(8);

        doReturn(Optional.empty()).when(accountRepository).findById(accountId);

        Optional<Account> account = persistence.findAccount(accountId);

        assertEquals(Optional.empty(), account);
    }
}
