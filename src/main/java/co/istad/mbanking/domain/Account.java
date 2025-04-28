package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String aliasName;

    @Column(length = 9, nullable = false, unique = true)
    private String actNo;

    @Column(nullable = false)
    private BigDecimal balance;
    @Column(nullable = false)
    private BigDecimal transferLimit;

    @Column(nullable = false)
    private Boolean isHidden;
    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne
    private AccountType accountType;

    @OneToOne
    private Card card;

    @OneToOne(mappedBy = "account")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "owner")
    private List<Transaction> transactionOfOwner;

}
