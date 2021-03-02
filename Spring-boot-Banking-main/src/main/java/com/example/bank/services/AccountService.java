package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account getAccount(String accountNumber) {
        Optional<Account> account = accountRepository
                .findByAccountNumber(accountNumber);

        account.ifPresent(value ->
                value.setTransactions(transactionRepository
                        .findByTransactionIdOrderByInitiationDate(value.getId())));

        return account.orElse(null);
    }
}
