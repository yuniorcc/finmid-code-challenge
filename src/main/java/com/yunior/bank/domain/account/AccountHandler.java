package com.yunior.bank.domain.account;

import com.yunior.bank.domain.account.model.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountHandler {

    private final AccountPersistence accountPersistence;

    public Optional<Account> createAccount(BigDecimal balance) {

        return accountPersistence.createAccount(balance);
    }

    public Optional<Account> getAccount(String accountId) {

        return accountPersistence.findAccount(accountId);
    }
}
