package mappers;

import dtos.ResponseUserDTO;
import models.User;
import org.springframework.stereotype.Component;

@Component
public class ResponseUserMapper {
    public ResponseUserDTO map(User user) {
        return new ResponseUserDTO(
                user.getLogin(),
                user.getPassword(),
                user.getName(),
                user.getSex(),
                user.getAge(),
                user.getHairColor(),
                user.getFriends());
    }
}
