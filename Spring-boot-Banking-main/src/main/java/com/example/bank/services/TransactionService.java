package com.example.bank.services;

import com.example.bank.models.Account;
import com.example.bank.models.Transaction;
import com.example.bank.repositories.AccountRepository;
import com.example.bank.repositories.TransactionRepository;
import com.example.bank.utils.TransactionInput;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public boolean makeTransfer(TransactionInput transactionInput) {
        // TODO refactor synchronous implementation with messaging 
        String sourceAccountNumber = transactionInput.getSourceAccount().getAccountNumber();
        Optional<Account> sourceAccount = accountRepository
                .findByAccountNumber(sourceAccountNumber);

        //String targetSortCode = transactionInput.getTargetAccount().getSortCode();
        String targetAccountNumber = transactionInput.getTargetAccount().getAccountNumber();
        Optional<Account> targetAccount = accountRepository
                .findByAccountNumber(targetAccountNumber);

        if (sourceAccount.isPresent() && targetAccount.isPresent()) {
            if (isAmountAvailable(transactionInput.getAmount(), sourceAccount.get().getCurrentBalance())) {
                var transaction = new Transaction();
                transaction.setTransactiond(sourceAccount.get().getId());
                transaction.setAmount(transactionInput.getAmount());
                transaction.setSourceAccountId(sourceAccount.get().getAccountNumber());
                transaction.setTargetAccountId(targetAccount.get().getAccountNumber());
                transaction.setTargetOwnerName(targetAccount.get().getOwnerName());
                transaction.setInitiationDate(LocalDateTime.now());
                transaction.setCompletionDate(LocalDateTime.now());
                transaction.setReference(transactionInput.getReference());
               
                updateAccountBalance(sourceAccount.get(), transactionInput.getAmount());
                transactionRepository.save(transaction);
                if((sourceAccount.get().getBankName()).equals((targetAccount.get().getBankName())))
                {
                	var transaction1 = new Transaction();
                	System.out.println("samebank=================");
                	System.out.println(sourceAccount.get().getBankName());
                	transaction1.setAmount(transactionInput.getAmount());
                	transaction1.setSourceAccountId(sourceAccount.get().getAccountNumber());
                	transaction1.setTargetAccountId(targetAccount.get().getAccountNumber());
                	transaction1.setTargetOwnerName(targetAccount.get().getOwnerName());
                	transaction1.setInitiationDate(LocalDateTime.now());
                	transaction1.setCompletionDate(LocalDateTime.now());
                	transaction1.setReference(transactionInput.getReference());
                	transaction1.setTransactiond(targetAccount.get().getId());
                	updatetargetAccountBalance(targetAccount.get(),transactionInput.getAmount());
                	transactionRepository.save(transaction1);
                }

                return true;
            }
        }
        return false;
    }

    private void updateAccountBalance(Account account, double amount) {
        account.setCurrentBalance((account.getCurrentBalance() - amount));
        accountRepository.save(account);
    }
    private void updatetargetAccountBalance(Account account, double amount) {
        account.setCurrentBalance((account.getCurrentBalance() + amount));
        accountRepository.save(account);
    }


    // TODO support overdrafts or credit account
    private boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
}
