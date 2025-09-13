package auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseFriendDTO {
    private String friendLogin;
    private ResponseUserDTO friendOwner;
}