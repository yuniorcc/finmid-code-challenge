package com.yunior.bank.controller.transaction.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionRequest {

    @Schema(
        description = "The transaction amount",
        example = "250.50",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Positive(message = "The amount must be a value greater than zero.")
    private final BigDecimal amount;

    @Schema(
        description = "The transaction's source account id.",
        example = "source-account-id",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "The transaction's source account id cannot be blank.")
    private final String from;

    @Schema(
        description = "The transaction's destination account id.",
        example = "destination-account-id",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(message = "The transaction's destination account id cannot be blank.")
    private final String to;
}
