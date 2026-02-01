package com.minibank.config;

import com.minibank.repositories.AccountRepository;
import com.minibank.repositories.InMemoryAccountRepository;
import com.minibank.repositories.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AccountRepository accountRepository() {
        return new InMemoryAccountRepository();
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return new TransactionRepository();
    }
}
