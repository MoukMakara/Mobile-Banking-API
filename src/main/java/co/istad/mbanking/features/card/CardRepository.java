package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {
    // Check if a card number already exists
    boolean existsByNumber(String number);

    // Find card by number
    Optional<Card> findByNumber(String number);

    // Find card by holder name
    List<Card> findByHolder(String holder);

    // Find active (not deleted) cards
    List<Card> findByIsDeletedFalse();

    // Find all cards by CardType
    List<Card> findByCardTypeId(Integer cardTypeId);

    // Find expired cards
    List<Card> findByExpiredAtBefore(LocalDate date);

    // Find valid (non-expired) cards
    List<Card> findByExpiredAtAfter(LocalDate date);
}