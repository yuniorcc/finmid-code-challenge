package com.yunior.bank.controller.transaction;

import com.yunior.bank.controller.transaction.request.TransactionRequest;
import com.yunior.bank.controller.transaction.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface TransactionApi {

    @Operation(
        summary = "Creates a new transaction",
        description = "Creates a new transaction between two accounts."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction correctly created"),
        @ApiResponse(responseCode = "409", description = "Transaction failed")
    })
    ResponseEntity<TransactionResponse> createTransaction(TransactionRequest transactionRequest);
}
