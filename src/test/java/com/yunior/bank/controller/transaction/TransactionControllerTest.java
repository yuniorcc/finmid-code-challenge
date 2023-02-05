package com.yunior.bank.controller.transaction;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.yunior.bank.controller.account.request.AccountRequest;
import com.yunior.bank.controller.transaction.request.TransactionRequest;
import com.yunior.bank.persistance.transaction.repository.TransactionRepository;
import jakarta.servlet.ServletException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    void post_transaction_shouldCreateTransaction() throws Exception {

        BigDecimal transactionAmount = new BigDecimal(676.14);
        BigDecimal sourceBalance = new BigDecimal(5546.25);
        BigDecimal destinationBalance = new BigDecimal(245.58);

        String sourceAccountId = createAccount(sourceBalance);
        String destinationAccountId = createAccount(destinationBalance);

        double expectedSourceBalance = sourceBalance.subtract(transactionAmount).doubleValue();
        double expectedDestinationBalance = destinationBalance.add(transactionAmount).doubleValue();

        createTransaction(transactionAmount, sourceAccountId, destinationAccountId, expectedSourceBalance, expectedDestinationBalance);

        verifyBalance(sourceAccountId, expectedSourceBalance);
        verifyBalance(destinationAccountId, expectedDestinationBalance);
    }

    @Test
    void post_transaction_verifyTransactionAtomicity() throws Exception {

        String sourceAccountId = createAccount(new BigDecimal(100));
        String destinationAccountId = createAccount(new BigDecimal(200));

        doThrow(IllegalArgumentException.class).when(transactionRepository).save(any());

        createTransactionAttempt(sourceAccountId, destinationAccountId);

        verifyBalance(sourceAccountId, 100);
        verifyBalance(destinationAccountId, 200);
    }

    private String createAccount(BigDecimal balance) throws Exception {

        AccountRequest accountRequest = new AccountRequest(balance);
        MvcResult result = mockMvc
            .perform(post("/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(accountRequest))
            )
            .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.accountId");
    }

    private void createTransaction(BigDecimal amount,
                                   String sourceAccountId,
                                   String destinationAccountId,
                                   double expectedSourceBalance,
                                   double expectedDestinationBalance) throws Exception {

        TransactionRequest request = new TransactionRequest(amount, sourceAccountId, destinationAccountId);
        mockMvc.perform(post("/transactions")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sourceAccountBalance", is(expectedSourceBalance), Double.class))
            .andExpect(jsonPath("$.destinationAccountBalance", is(expectedDestinationBalance), Double.class));
    }

    private void createTransactionAttempt(String sourceAccountId, String destinationAccountId) throws Exception {

        TransactionRequest request = new TransactionRequest(new BigDecimal(10), sourceAccountId, destinationAccountId);
        assertThrows(ServletException.class, () -> mockMvc.perform(post("/transactions")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
        );
    }

    private void verifyBalance(String accountId, double expectedBalance) throws Exception {

        mockMvc
            .perform(get("/accounts/{accountId}", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId", is(accountId)))
            .andExpect(jsonPath("$.balance", is(expectedBalance), Double.class))
            .andReturn();
    }

    @Test
    void post_transaction_shouldReturnConflictWhenAccountsNotFound() throws Exception {

        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(687), RandomStringUtils.random(8), RandomStringUtils.random(8));
        mockMvc
            .perform(post("/transactions")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transactionRequest))
            )
            .andExpect(status().isConflict());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTransactions")
    void post_transaction_shouldReturnBadRequestWhenTransactionIsInvalid(double amount, String from, String to, String expectedErrorMessage) throws Exception {

        TransactionRequest transactionRequest = new TransactionRequest(new BigDecimal(amount), from, to);
        mockMvc
            .perform(post("/transactions")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(transactionRequest))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem(expectedErrorMessage)));
    }

    private static Stream<Arguments> provideInvalidTransactions() {

        return Stream.of(
            Arguments.of(0, RandomStringUtils.random(8), RandomStringUtils.random(8), "amount: The amount must be a value greater than zero."),
            Arguments.of(-1, RandomStringUtils.random(8), RandomStringUtils.random(8), "amount: The amount must be a value greater than zero."),
            Arguments.of(1, null, RandomStringUtils.random(8), "from: The transaction's source account id cannot be blank."),
            Arguments.of(1, "", RandomStringUtils.random(8), "from: The transaction's source account id cannot be blank."),
            Arguments.of(1, RandomStringUtils.random(8), null, "to: The transaction's destination account id cannot be blank."),
            Arguments.of(1, RandomStringUtils.random(8), "", "to: The transaction's destination account id cannot be blank.")
        );
    }
}
