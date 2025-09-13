package controllers;

import dtos.BankAccountDTO;
import dtos.ResponseBankAccountDTO;
import dtos.ResponseOperationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mappers.BankAccountMapper;
import mappers.ResponseBankAccountMapper;
import mappers.ResponseOperationMapper;
import models.BankAccount;
import models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.BankAccountServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Bank Account Operations", description = "Методы для работы с банковскими счетами")
public class BankAccountController {

    private final BankAccountServiceImpl bankAccountService;
    private final BankAccountMapper bankAccountMapper;
    private final ResponseBankAccountMapper responseBankAccountMapper;
    private final ResponseOperationMapper responseOperationMapper;

    @Autowired
    public BankAccountController(BankAccountServiceImpl bankAccountService,
                                 BankAccountMapper bankAccountMapper,
                                 ResponseBankAccountMapper responseBankAccountMapper,
                                 ResponseOperationMapper responseOperationMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
        this.responseBankAccountMapper = responseBankAccountMapper;
        this.responseOperationMapper = responseOperationMapper;
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Создание нового банковского счёта", description = "Создание нового счёта на основе переданных данных.")
    @ApiResponse(responseCode = "201", description = "Счёт успешно создан")
    @PostMapping
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountMapper.map(bankAccountDTO);
        BankAccount createdBankAccount = bankAccountService.createBankAccount(bankAccount);
        return new ResponseEntity<>(createdBankAccount, HttpStatus.CREATED);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Получение информации о счёте", description = "Получение информации о банковском счёте по его ID.")
    @ApiResponse(responseCode = "200", description = "Информация о счёте получена")
    @ApiResponse(responseCode = "404", description = "Счёт не найден")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBankAccountDTO> getBankAccount(@PathVariable("id") Integer id) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id);
        ResponseBankAccountDTO responseBankAccountDTO = responseBankAccountMapper.map(bankAccount);
        return new ResponseEntity<>(responseBankAccountDTO, HttpStatus.OK);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Получение операций по счёту", description = "Получение списка операций по банковскому счёту по его ID.")
    @ApiResponse(responseCode = "200", description = "Список операций получен")
    @ApiResponse(responseCode = "204", description = "Операции не найдены")
    @GetMapping("/{id}/operations")
    public ResponseEntity<List<ResponseOperationDTO>> getOperations(@PathVariable("id") Integer id) {
        List<Transaction> transactions = bankAccountService.getOperations(id);
        List<ResponseOperationDTO> responseList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            responseList.add(responseOperationMapper.map(transaction));
        }
        if (!transactions.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Пополнение счёта", description = "Пополнение баланса банковского счёта.")
    @ApiResponse(responseCode = "200", description = "Счёт успешно пополнен")
    @ApiResponse(responseCode = "400", description = "Некорректная сумма для пополнения")
    @PostMapping("/{id}/top-up")
    public ResponseEntity<Void> topUpAccount(@PathVariable("id") Integer id,
                                             @RequestParam(value = "amount", required = false) Double amount) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id);
        Transaction newTransaction = new Transaction(amount,"Снятие", bankAccount);
        bankAccountService.topUpAccount(newTransaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Снятие денег со счёта", description = "Снятие определённой суммы со счёта.")
    @ApiResponse(responseCode = "200", description = "Деньги успешно сняты")
    @ApiResponse(responseCode = "400", description = "Некорректная сумма для снятия")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawFromAccount(@PathVariable("id") Integer id,
                                                    @RequestParam(value = "amount", required = false) Double amount) {
        BankAccount bankAccount = bankAccountService.getBankAccount(id);
        Transaction newTransaction = new Transaction(amount,"Снятие", bankAccount);
        bankAccountService.withdrawFromAccount(newTransaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Перевод денег на другой счёт", description = "Перевод средств с одного счёта на другой.")
    @ApiResponse(responseCode = "200", description = "Деньги успешно переведены")
    @ApiResponse(responseCode = "400", description = "Некорректная сумма для перевода")
    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<Void> transferMoney(
            @PathVariable("fromAccountId") Integer fromAccountId,
            @PathVariable("toAccountId") Integer toAccountId,
            @RequestParam(value = "amount", required = false) Double amount) {
        BankAccount fromBankAccount = bankAccountService.getBankAccount(fromAccountId);
        Transaction newTransaction = new Transaction(amount,"Снятие", fromBankAccount);
        bankAccountService.transferMoney(toAccountId, newTransaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Получение списка всех счётов", description = "Получение списка всех банковских счетов.")
    @ApiResponse(responseCode = "200", description = "Список счётов получен")
    @ApiResponse(responseCode = "204", description = "Счёты не найдены")
    @GetMapping
    public ResponseEntity<List<ResponseBankAccountDTO>> getAllBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        List<ResponseBankAccountDTO> responseList = new ArrayList<>();
        for (BankAccount bankAccount : bankAccounts) {
            responseList.add(responseBankAccountMapper.map(bankAccount));
        }
        if (bankAccounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Tag(name = "Bank Account Operations")
    @Operation(summary = "Получение операций по фильтру", description = "Получение списка операций по типу (например, пополнение, снятие) и ID счёта.")
    @ApiResponse(responseCode = "200", description = "Список операций получен")
    @ApiResponse(responseCode = "204", description = "Операции не найдены")
    @GetMapping("/{accountId}/operations-with-filter")
    public ResponseEntity<List<ResponseOperationDTO>> getOperationsWithFilter(
            @PathVariable("accountId") Integer accountId,
            @RequestParam(value = "operation", required = false) String operation) {
        BankAccount bankAccount = bankAccountService.getBankAccount(accountId);
        List<Transaction> transactions = bankAccountService.getOperationsWithFilter(operation, bankAccount);
        List<ResponseOperationDTO> responseList = new ArrayList<>();
        for (Transaction transactionDTO : transactions) {
            responseList.add(responseOperationMapper.map(transactionDTO));
        }
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}