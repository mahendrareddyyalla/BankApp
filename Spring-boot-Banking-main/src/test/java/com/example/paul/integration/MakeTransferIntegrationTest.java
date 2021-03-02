package com.example.paul.integration;

import com.example.bank.controllers.TransactionRestController;
import com.example.bank.utils.AccountInput;
import com.example.bank.utils.TransactionInput;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
class MakeTransferIntegrationTest {

    @Autowired
    private TransactionRestController transactionRestController;

    @Test
    void givenTransactionDetails_whenMakeTransaction_thenVerifyTransactionIsProcessed() {
        // given
        var sourceAccount = new AccountInput();
       
        sourceAccount.setAccountNumber("73084635");

        var targetAccount = new AccountInput();
        
        targetAccount.setAccountNumber("21956204");

        var input = new TransactionInput();
        input.setSourceAccount(sourceAccount);
        input.setTargetAccount(targetAccount);
        input.setAmount(27.5);
        input.setReference("My reference");
        
        // when
        var body = transactionRestController.makeTransfer(input).getBody();

        // then
        var isComplete = (Boolean) body;
        assertThat(isComplete).isTrue();
    }
}
