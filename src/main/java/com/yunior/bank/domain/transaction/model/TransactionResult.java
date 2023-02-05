package com.yunior.bank.domain.transaction.model;

import java.math.BigDecimal;

public record TransactionResult(BigDecimal sourceAccountBalance, BigDecimal destinationAccountBalance) {
}
