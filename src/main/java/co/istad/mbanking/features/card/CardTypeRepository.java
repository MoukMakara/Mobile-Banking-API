package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
    // Find card type by alias
    Optional<CardType> findByAlias(String alias);

    // Find card type by name
    Optional<CardType> findByName(String name);

    // Find all active (not deleted) card types
    List<CardType> findByIsDeletedFalse();

    // Check if card type alias exists
    boolean existsByAlias(String alias);

    // Check if card type name exists
    boolean existsByName(String name);
}
