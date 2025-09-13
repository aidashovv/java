package auth.controllers;

import auth.domains.enums.HairColor;
import auth.dtos.*;
import auth.services.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminService;

    @Autowired
    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create/user")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody SignUpRequest signUpRequest) {
        ResponseUserDTO responseUserDTO = adminService.createUser(signUpRequest);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @PostMapping("/create/admin")
    public ResponseEntity<ResponseUserDTO> createAdmin(@RequestBody SignUpRequest signUpRequest) {
        ResponseUserDTO responseUserDTO = adminService.createAdmin(signUpRequest);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{login}")
    public ResponseEntity<ResponseUserDTO> getUser(@PathVariable String login) {
        ResponseUserDTO responseUserDTO = adminService.getUser(login);
        if (responseUserDTO != null) {
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user-list-with-filter")
    public ResponseEntity<List<ResponseUserDTO>> getUsersWithFilter(
            @RequestParam(required = false) String hairColor,
            @RequestParam(required = false) String sex) {

        List<ResponseUserDTO> users = adminService.getUsersWithFilter(
                hairColor != null ? HairColor.valueOf(hairColor.toUpperCase()) : null,
                sex
        );

        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/bank-accounts-list")
    public ResponseEntity<List<ResponseBankAccountDTO>> getAllBankAccounts() {
        List<ResponseBankAccountDTO> bankAccounts = adminService.getAllBankAccounts();
        return new ResponseEntity<>(bankAccounts, HttpStatus.OK);
    }

    @GetMapping("/bank-account/{login}/operations")
    public ResponseEntity<List<ResponseTransactionDTO>> getOperations(@PathVariable String login) {
        List<ResponseTransactionDTO> operations = adminService.getOperations(login);
        if (!operations.isEmpty()) {
            return new ResponseEntity<>(operations, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/deauthorize")
    public ResponseEntity<Void> deauthorize() {
        adminService.deauthorize();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}