package com.yunior.bank.persistance.transaction;

import com.yunior.bank.domain.transaction.TransactionPersistence;
import com.yunior.bank.domain.transaction.model.Transaction;
import com.yunior.bank.domain.transaction.model.TransactionResult;
import com.yunior.bank.persistance.account.entity.AccountEntity;
import com.yunior.bank.persistance.account.repository.AccountRepository;
import com.yunior.bank.persistance.transaction.entity.TransactionEntity;
import com.yunior.bank.persistance.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DatabaseTransactionPersistence implements TransactionPersistence {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public Optional<TransactionResult> createTransaction(Transaction transaction) {

        Optional<AccountEntity> source = accountRepository.findById(transaction.source());
        if (source.isPresent()) {
            Optional<AccountEntity> destination = accountRepository.findById(transaction.destination());
            if (destination.isPresent()) {
                return createTransaction(transaction.amount(), source.get(), destination.get());
            }
        }
        return Optional.empty();
    }

    private Optional<TransactionResult> createTransaction(BigDecimal amount, AccountEntity source, AccountEntity destination) {

        BigDecimal sourceBalance = source.getBalance().subtract(amount);
        BigDecimal destinationBalance = destination.getBalance().add(amount);

        source.setBalance(sourceBalance);
        destination.setBalance(destinationBalance);

        accountRepository.save(source);
        accountRepository.save(destination);

        TransactionEntity transaction = new TransactionEntity(amount, source.getAccountId(), destination.getAccountId());
        transactionRepository.save(transaction);

        return Optional.of(new TransactionResult(source.getBalance(), destination.getBalance()));
    }
}
