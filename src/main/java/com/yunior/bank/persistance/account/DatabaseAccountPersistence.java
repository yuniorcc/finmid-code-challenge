package com.yunior.bank.persistance.account;

import com.yunior.bank.domain.account.AccountPersistence;
import com.yunior.bank.domain.account.model.Account;
import com.yunior.bank.persistance.account.entity.AccountEntity;
import com.yunior.bank.persistance.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DatabaseAccountPersistence implements AccountPersistence {

    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> createAccount(BigDecimal balance) {

        AccountEntity accountEntity = new AccountEntity(balance);
        accountEntity = accountRepository.save(accountEntity);
        Account account = new Account(accountEntity.getAccountId(), accountEntity.getBalance());
        return Optional.of(account);
    }

    @Override
    public Optional<Account> findAccount(String accountId) {

        Optional<AccountEntity> accountEntity = accountRepository.findById(accountId);
        return accountEntity.map(a -> new Account(a.getAccountId(), a.getBalance()));
    }
}
