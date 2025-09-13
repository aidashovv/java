package org.aidashovv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repositories.BankAccountRepository;
import repositories.FriendRepository;
import repositories.TransactionRepository;
import repositories.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"models", "controllers", "services", "repositories", "mappers", "handlers"})
@EnableJpaRepositories(basePackageClasses = {BankAccountRepository.class,
        UserRepository.class, TransactionRepository.class, FriendRepository.class})
@EntityScan(basePackages = "models")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
