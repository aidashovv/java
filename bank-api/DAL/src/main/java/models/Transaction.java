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
@Table(name = "operations")
public class Transaction {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "description", nullable = false, length = 50)
    private String operation;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    public Transaction(Double amount, String operation, BankAccount bankAccount) {
        this.operation = operation;
        this.amount = amount;
        this.bankAccount = bankAccount;
    }
}
