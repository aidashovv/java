package models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "owner_login", nullable = false)
    private String ownerLogin;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    public BankAccount(Double balance, String ownerLogin) {
        this.balance = balance;
        this.ownerLogin = ownerLogin;
    }
}