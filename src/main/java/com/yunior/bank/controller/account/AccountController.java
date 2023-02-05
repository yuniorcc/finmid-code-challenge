package com.yunior.bank.controller.account;

import com.yunior.bank.controller.account.request.AccountRequest;
import com.yunior.bank.controller.account.response.AccountResponse;
import com.yunior.bank.domain.account.AccountHandler;
import com.yunior.bank.domain.account.model.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class AccountController implements AccountApi {

    private final AccountHandler accountHandler;

    @Override
    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {

        Optional<Account> account = accountHandler.createAccount(accountRequest.getBalance());
        return account.map(a -> new AccountResponse(a.accountId(), a.balance()))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @Override
    @GetMapping(value = "/accounts/{accountId}", produces = "application/json")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountId) {

        Optional<Account> account = accountHandler.getAccount(accountId);
        return account.map(a -> new AccountResponse(a.accountId(), a.balance()))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
