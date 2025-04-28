package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String holder;

    @Column(nullable = false, unique = true, length = 16)
    private String number;

    @Column(nullable = false, length = 3)
    private String cvv;

    @Column(nullable = false)
    private LocalDate issuedAt;
    @Column(nullable = false)
    private LocalDate expiredAt;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToOne(mappedBy = "card")
    private Account account;

    @ManyToOne
    private CardType cardType;

}
