package com.yunior.bank.domain.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.yunior.bank.domain.account.model.Account;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountHandlerTest {

    private AccountPersistence accountPersistence;
    private AccountHandler accountHandler;

    @BeforeAll
    void setup() {

        accountPersistence = mock(AccountPersistence.class);
        accountHandler = new AccountHandler(accountPersistence);
    }

    @Test
    void createAccount_shouldCallAccountPersistence() {

        BigDecimal balance = new BigDecimal(8627);
        Account expectedPersistedAccount = new Account(RandomStringUtils.random(8), balance);

        doReturn(Optional.of(expectedPersistedAccount)).when(accountPersistence).createAccount(balance);

        Optional<Account> account = accountHandler.createAccount(balance);

        assertEquals(expectedPersistedAccount, account.get());
    }

    @Test
    void getAccount_shouldCallAccountPersistence() {

        String accountId = RandomStringUtils.random(8);
        Account expectedAccountFound = new Account(accountId, new BigDecimal(93174));

        doReturn(Optional.of(expectedAccountFound)).when(accountPersistence).findAccount(accountId);

        Optional<Account> account = accountHandler.getAccount(accountId);

        assertEquals(expectedAccountFound, account.get());
    }
}
