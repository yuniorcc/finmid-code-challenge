package com.yunior.bank.domain.account;

import com.yunior.bank.domain.account.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountPersistence {

    Optional<Account> createAccount(BigDecimal balance);

    Optional<Account> findAccount(String accountId);
}
