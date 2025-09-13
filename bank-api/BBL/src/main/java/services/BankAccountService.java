package services;

import models.BankAccount;
import models.Transaction;

import java.util.List;

public interface BankAccountService {

    BankAccount createBankAccount(BankAccount account);

    void topUpAccount(Transaction transaction);

    void withdrawFromAccount(Transaction transaction);

    void transferMoney(Integer toAccountId, Transaction transaction);

    BankAccount getBankAccount(Integer id);

    List<Transaction> getOperations(Integer id);

    List<BankAccount> getAllBankAccounts();

    List<Transaction> getOperationsWithFilter(String operation, BankAccount bankAccount);
}
