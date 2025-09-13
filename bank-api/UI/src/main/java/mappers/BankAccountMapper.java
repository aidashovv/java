package mappers;

import dtos.BankAccountDTO;
import models.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    public BankAccount map(BankAccountDTO bankAccountDTO) {
        return new BankAccount(
                bankAccountDTO.getBalance(),
                bankAccountDTO.getOwnerLogin());
    }
}
