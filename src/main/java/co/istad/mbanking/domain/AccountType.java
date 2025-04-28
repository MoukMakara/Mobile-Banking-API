package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account_types")
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true, length = 50)
    private String alias; // payroll-account
    @Column(nullable = false, unique = true, length = 25)
    private String name; // Payroll Account
    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isDeleted;

    @OneToMany(mappedBy = "accountType")
    private List<Account> accounts;

}
