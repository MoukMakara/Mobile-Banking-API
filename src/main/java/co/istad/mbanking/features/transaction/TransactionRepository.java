package co.istad.mbanking.features.transaction;

import co.istad.mbanking.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
