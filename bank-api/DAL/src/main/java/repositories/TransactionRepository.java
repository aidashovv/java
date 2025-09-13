package repositories;

import models.BankAccount;
import models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<List<Transaction>> findByOperationAndBankAccount(String operationType, BankAccount bankAccount);
}
