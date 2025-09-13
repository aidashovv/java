package mappers;

import models.User;
import org.springframework.stereotype.Component;
import dtos.CreateUserRequestDTO;

@Component
public class CreateUserRequestMapper {
    public User map(CreateUserRequestDTO createUserRequestDTO) {
        return new User(
                createUserRequestDTO.getUsername(),
                createUserRequestDTO.getPassword(),
                createUserRequestDTO.getName(),
                createUserRequestDTO.getSex(),
                createUserRequestDTO.getAge(),
                createUserRequestDTO.getHairColor());
    }
}
