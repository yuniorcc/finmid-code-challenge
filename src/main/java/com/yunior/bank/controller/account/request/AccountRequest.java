package com.yunior.bank.controller.account.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountRequest {

    @Schema(
        description = "The account balance",
        example = "246.87",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Positive(message = "The balance must be a value greater than zero.")
    private final BigDecimal balance;

    @JsonCreator
    public AccountRequest(BigDecimal balance) {
        this.balance = balance;
    }
}
