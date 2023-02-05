package com.yunior.bank.domain.transaction.model;

import java.math.BigDecimal;

public record Transaction(BigDecimal amount, String source, String destination) {
}
