package co.istad.mbanking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "card_types")
public class CardType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String alias; // visa-card
    @Column(unique = true, nullable = false, length = 50)
    private String name; // Visa Card

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "cardType")
    private List<Card> cards;

}
