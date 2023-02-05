package com.yunior.bank.persistance.account.repository;

import com.yunior.bank.persistance.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
}
