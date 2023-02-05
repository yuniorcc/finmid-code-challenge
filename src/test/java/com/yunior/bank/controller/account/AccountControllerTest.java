package com.yunior.bank.controller.account;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.yunior.bank.controller.account.request.AccountRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0, -3.14})
    void post_account_shouldReturnBadRequestWhenBalanceIsZeroOrNegative(double balance) throws Exception {

        AccountRequest accountRequest = new AccountRequest(new BigDecimal(balance));

        mockMvc
            .perform(post("/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(accountRequest))
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasItem("balance: The balance must be a value greater than zero.")));
    }

    @ParameterizedTest
    @ValueSource(doubles = {575.7, 13, 22.55, 100011, 10000000000000.99})
    void post_account_shouldCreateAndReturnAccount(double balance) throws Exception {

        String accountId = performPostOperation(balance);
        performGetOperation(accountId, balance);
    }

    private String performPostOperation(double balance) throws Exception {

        AccountRequest accountRequest = new AccountRequest(new BigDecimal(balance));

        MvcResult result = mockMvc
            .perform(post("/accounts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(accountRequest))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId", notNullValue()))
            .andExpect(jsonPath("$.balance", is(balance), Double.class))
            .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.accountId");
    }

    private void performGetOperation(String accountId, double expectedBalance) throws Exception {

        mockMvc
            .perform(get("/accounts/{accountId}", accountId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountId", is(accountId)))
            .andExpect(jsonPath("$.balance", is(expectedBalance), Double.class))
            .andReturn();
    }
}
