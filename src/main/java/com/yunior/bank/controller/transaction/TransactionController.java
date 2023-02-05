package com.yunior.bank.controller.transaction;

import com.yunior.bank.controller.transaction.request.TransactionRequest;
import com.yunior.bank.controller.transaction.response.TransactionResponse;
import com.yunior.bank.domain.transaction.TransactionHandler;
import com.yunior.bank.domain.transaction.model.TransactionResult;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionHandler transactionHandler;

    @Override
    @PostMapping(value = "/transactions", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {

        Optional<TransactionResult> result = transactionHandler.createTransaction(request.getAmount(), request.getFrom(), request.getTo());
        return result.map(r -> new TransactionResponse(r.sourceAccountBalance(), r.destinationAccountBalance()))
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}
