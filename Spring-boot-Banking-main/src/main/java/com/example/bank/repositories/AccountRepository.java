package com.example.bank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bank.models.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    	Optional<Account> findByAccountNumber(String accountNumber);
}
