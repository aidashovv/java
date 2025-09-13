package dtos;

import enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {
    private String username;
    private String name;
    private String password;
    private String sex;
    private Integer age;
    private HairColor hairColor;
}
