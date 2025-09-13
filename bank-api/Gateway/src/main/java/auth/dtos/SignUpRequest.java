package auth.dtos;

import auth.domains.enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String name;
    private String sex;
    private Integer age;
    private HairColor hairColor;
}
