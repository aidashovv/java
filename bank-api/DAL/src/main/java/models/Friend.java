package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friends")
public class Friend {

    @Id
    @Column(name = "friend_login", nullable = false, length = 50)
    private String friendLogin;

    @ManyToOne
    @JoinColumn(name = "owner_login", referencedColumnName = "login")
    private User friendOwner;
}
