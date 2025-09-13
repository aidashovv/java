package auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BankAccountDTO {
    private Double balance;
    private String ownerLogin;
}
