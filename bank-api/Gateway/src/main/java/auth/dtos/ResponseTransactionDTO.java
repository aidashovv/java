package auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseTransactionDTO {
    private Integer id;
    private Double amount;
    private String operation;
}
