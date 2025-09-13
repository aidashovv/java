package mappers;

import dtos.ResponseBankAccountDTO;
import models.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class ResponseBankAccountMapper {
    public ResponseBankAccountDTO map(BankAccount bankAccount) {
        return new ResponseBankAccountDTO(
                bankAccount.getId(),
                bankAccount.getBalance(),
                bankAccount.getOwnerLogin());
    }
}
