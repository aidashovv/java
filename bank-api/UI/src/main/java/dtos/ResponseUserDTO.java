package dtos;

import enums.HairColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.Friend;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUserDTO {
    private String username;
    private String password;
    private String name;
    private String sex;
    private Integer age;
    private HairColor hairColor;
    private List<Friend> friends;
}
