package com.yunior.bank.persistance.transaction.repository;

import com.yunior.bank.persistance.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
}
