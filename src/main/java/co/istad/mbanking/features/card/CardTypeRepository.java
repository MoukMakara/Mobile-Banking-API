package co.istad.mbanking.features.card;

import co.istad.mbanking.domain.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
    Optional<CardType> findByAlias(String alias);
    boolean existsByAlias(String alias);
}
