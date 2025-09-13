package models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import enums.HairColor;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "login")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "sex", nullable = false, length = 10)
    private String sex;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "hair_color", nullable = false)
    private HairColor hairColor;

    @OneToMany(mappedBy = "friendOwner", fetch = FetchType.EAGER)
    private List<Friend> friends;

    public User(String login, String password, String name, String sex,
                Integer age, HairColor hairColor) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.hairColor = hairColor;
    }
}