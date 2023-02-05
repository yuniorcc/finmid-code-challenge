package com.yunior.bank.controller.account;

import com.yunior.bank.controller.account.request.AccountRequest;
import com.yunior.bank.controller.account.response.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface AccountApi {

    @Operation(
        summary = "Creates a new account",
        description = "Creates a new account with initial balance."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account correctly created"),
        @ApiResponse(responseCode = "409", description = "Account creation failed")
    })
    ResponseEntity<AccountResponse> createAccount(AccountRequest accountRequest);

    @Operation(
        summary = "Gets account",
        description = "Gets the account matching the given accountId."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account correctly retrieved"),
        @ApiResponse(responseCode = "404", description = "Account was not found")
    })
    ResponseEntity<AccountResponse> getAccount(@Parameter(description = "Account id", required = true) String accountId);
}
