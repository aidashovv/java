package auth.controllers;

import auth.dtos.*;
import auth.services.ClientServiceImpl;
import auth.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;
    private final ClientServiceImpl clientService;

    @Autowired
    public UserController(UserServiceImpl userService, ClientServiceImpl clientService) {
        this.userService = userService;
        this.clientService = clientService;
    }

    @PostMapping("/{login}/top-up")
    public ResponseEntity<ResponseTransactionDTO> topUp(@PathVariable("login") String login,
                                                        @RequestBody TransactionDTO transactionDTO) {
        ResponseTransactionDTO response = userService.topUp(login, transactionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{login}/withdraw")
    public ResponseEntity<ResponseTransactionDTO> withdraw(@PathVariable("login") String login,
                                                           @RequestBody TransactionDTO transactionDTO) {
        ResponseTransactionDTO response = userService.withdraw(login, transactionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    public ResponseEntity<ResponseTransactionDTO> transferMoney(@PathVariable("fromAccountId") String fromAccountId,
                                                                @PathVariable("toAccountId") String toAccountId,
                                                                @RequestBody TransactionDTO transactionDTO) {
        ResponseTransactionDTO response = userService.transferMoney(fromAccountId, toAccountId, transactionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update-friend")
    public ResponseEntity<Void> updateFriendList(@RequestBody FriendRequest friendRequest) {
        userService.updateFriendList(clientService.getCurrentUser().getUsername(), friendRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseUserDTO> getUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseUserDTO response = userService.getUser(login);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{login}/bank-account")
    public ResponseEntity<ResponseBankAccountDTO> getBankAccount(@PathVariable("login") String login) {
        ResponseBankAccountDTO response = userService.getBankAccount(login);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/deauthorize")
    public ResponseEntity<Void> deauthorize() {
        userService.deauthorize();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}