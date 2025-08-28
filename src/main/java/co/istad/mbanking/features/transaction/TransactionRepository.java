package co.istad.mbanking.features.transaction;

import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Find transactions where the account is either the owner or receiver
    @Query("SELECT t FROM Transaction t WHERE (t.owner.actNo = :actNo OR t.receiver.actNo = :actNo) AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByAccountNumber(String actNo, Pageable pageable);

    // Find all transactions for admin/staff roles
    @Query("SELECT t FROM Transaction t WHERE t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findAllTransactions(Pageable pageable);

    // Find all transactions for all accounts belonging to a specific user
    @Query("SELECT t FROM Transaction t WHERE " +
           "(t.owner.userAccount.user = :user OR t.receiver.userAccount.user = :user) " +
           "AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByUser(User user, Pageable pageable);
}
