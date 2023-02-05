package com.yunior.bank.domain.account.model;

import java.math.BigDecimal;

public record Account(String accountId, BigDecimal balance) {
}
