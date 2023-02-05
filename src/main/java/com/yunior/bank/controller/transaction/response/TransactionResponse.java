package com.yunior.bank.controller.transaction.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionResponse {

    @Schema(
        description = "The source account balance",
        example = "250.50"
    )
    private final BigDecimal sourceAccountBalance;

    @Schema(
        description = "The destination account balance",
        example = "250.50"
    )
    private final BigDecimal destinationAccountBalance;
}
