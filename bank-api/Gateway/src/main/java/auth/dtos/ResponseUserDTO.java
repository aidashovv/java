package auth.dtos;

import auth.domains.enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDTO {
    private String username;
    private String name;
    private String sex;
    private Integer age;
    private HairColor hairColor;
    private List<ResponseFriendDTO> friends;
}
