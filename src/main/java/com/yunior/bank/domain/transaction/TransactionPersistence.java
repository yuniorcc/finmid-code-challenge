package com.yunior.bank.domain.transaction;

import com.yunior.bank.domain.transaction.model.Transaction;
import com.yunior.bank.domain.transaction.model.TransactionResult;

import java.util.Optional;

public interface TransactionPersistence {

    Optional<TransactionResult> createTransaction(Transaction transaction);
}
