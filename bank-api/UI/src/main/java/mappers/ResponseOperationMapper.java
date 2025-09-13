package mappers;

import dtos.ResponseOperationDTO;
import models.Transaction;
import org.springframework.stereotype.Component;

@Component
public class ResponseOperationMapper {
    public ResponseOperationDTO map(Transaction transaction) {
        return new ResponseOperationDTO(
                transaction.getAmount(),
                transaction.getOperation());
    }
}
