package auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionDTO {
    private Double amount;
    private String operation;
}
