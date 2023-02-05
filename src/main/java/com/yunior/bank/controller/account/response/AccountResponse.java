package com.yunior.bank.controller.account.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountResponse {

    @Schema(description = "The account id", example = "the-account-id")
    private final String accountId;

    @Schema(description = "The account balance", example = "246.87")
    private final BigDecimal balance;
}
