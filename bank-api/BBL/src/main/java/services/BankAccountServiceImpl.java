package services;

import models.BankAccount;
import models.Friend;
import models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.BankAccountRepository;
import repositories.TransactionRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository inputBankAccountRepository,
                                  UserService inputUserService,
                                  TransactionRepository inputTransactionRepository) {
        bankAccountRepository = inputBankAccountRepository;
        userService = inputUserService;
        transactionRepository = inputTransactionRepository;
    }

    @Override
    public BankAccount createBankAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    @Override
    public BankAccount getBankAccount(Integer id) {
        Optional<BankAccount> foundedBankAccount = bankAccountRepository.findById(id);
        if (foundedBankAccount.isEmpty()) {
            throw new NoSuchElementException("Счёт с данным ID не найден.");
        }
        return foundedBankAccount.get();
    }

    @Override
    public List<Transaction> getOperations(Integer id) {
        Optional<BankAccount> foundedBankAccount = bankAccountRepository.findById(id);
        if (foundedBankAccount.isEmpty()) {
            throw new NoSuchElementException("Счёт с данным ID не найден.");
        }
        BankAccount bankAccount = foundedBankAccount.get();
        return bankAccount.getTransactions();
    }

    @Override
    @Transactional
    public void topUpAccount(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть положительна.");
        }

        Integer bankAccountId = transaction.getBankAccount().getId();
        Optional<BankAccount> foundedBankAccount = bankAccountRepository.findById(bankAccountId);
        if (foundedBankAccount.isPresent()) {
            BankAccount bankAccount = foundedBankAccount.get();
            double newBalance = bankAccount.getBalance() + transaction.getAmount();
            bankAccount.setBalance(newBalance);
            bankAccount.getTransactions().add(transaction);
            transactionRepository.save(transaction);
        } else
            throw new NoSuchElementException("Счёт с данным ID не найден.");
    }

    @Override
    @Transactional
    public void withdrawFromAccount(Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть положительна.");
        }

        Integer bankAccountId = transaction.getBankAccount().getId();
        Optional<BankAccount> foundedBankAccount = bankAccountRepository.findById(bankAccountId);
        if (foundedBankAccount.isPresent()) {
            BankAccount bankAccount = foundedBankAccount.get();
            if (bankAccount.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Недостаточно средств.");
            }

            double newBalance = bankAccount.getBalance() - transaction.getAmount();
            bankAccount.setBalance(newBalance);
            bankAccount.getTransactions().add(transaction);
            transactionRepository.save(transaction);
        } else
            throw new NoSuchElementException("Счёт с данным ID не найден.");
    }

    @Override
    @Transactional
    public void transferMoney(Integer toAccountId, Transaction transaction) {
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительна.");
        }

        Optional<BankAccount> foundedToAccount = bankAccountRepository.findById(toAccountId);
        if (foundedToAccount.isPresent()) {
            BankAccount toAccount = foundedToAccount.get();
            if (transaction.getBankAccount().getOwnerLogin().equals(toAccount.getOwnerLogin())) {
                transaction.setOperation("Перевод на счет с id: " + toAccountId);
                withdrawFromAccount(transaction);

                String ownerLoginFromAccount = transaction.getBankAccount().getOwnerLogin();

                transaction.setBankAccount(toAccount);
                transaction.setOperation("Пополнение от: " + ownerLoginFromAccount);
                topUpAccount(transaction);
            } else {
                List<Friend> ownerFromAccountFriends = userService.getFriends(transaction.getBankAccount().getOwnerLogin());
                boolean isFriend = false;
                for (Friend ownerFromAccountFriend : ownerFromAccountFriends) {
                    if (toAccount.getOwnerLogin().equals(ownerFromAccountFriend.getFriendLogin())) {
                        isFriend = true;
                        break;
                    }
                }

                transaction.setOperation("Перевод на счет с id: " + toAccountId);

                double originalAmount = transaction.getAmount();
                double finalAmount = calculateCommission(isFriend, originalAmount);
                transaction.setAmount(finalAmount);

                withdrawFromAccount(transaction);

                String ownerLoginFromAccount = transaction.getBankAccount().getOwnerLogin();

                transaction.setBankAccount(toAccount);
                transaction.setOperation("Пополнение от: " + ownerLoginFromAccount);
                transaction.setAmount(originalAmount);

                topUpAccount(transaction);
            }
        } else
            throw new NoSuchElementException("Адресат с данным ID не найден.");
    }

    private double calculateCommission(boolean isFriend, double amount) {
        double commissionRate = isFriend ? 0.03 : 0.1;
        return amount * commissionRate + amount;
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public List<Transaction> getOperationsWithFilter(String operation, BankAccount bankAccount) {
        Optional<List<Transaction>> resultList =
                transactionRepository.findByOperationAndBankAccount(operation, bankAccount);
        return resultList.orElse(null);
    }
}
