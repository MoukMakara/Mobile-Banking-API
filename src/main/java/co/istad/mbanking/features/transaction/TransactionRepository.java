package co.istad.mbanking.features.transaction;

import co.istad.mbanking.domain.Transaction;
import co.istad.mbanking.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Find transactions where the account is either the owner or receiver
    @Query("SELECT t FROM Transaction t WHERE (t.owner.actNo = :actNo OR t.receiver.actNo = :actNo) AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByAccountNumber(String actNo, Pageable pageable);

    // Find transactions where the account is either the owner or receiver, filtered by transaction type
    @Query("SELECT t FROM Transaction t WHERE (t.owner.actNo = :actNo OR t.receiver.actNo = :actNo) AND (:transactionType IS NULL OR t.transactionType = :transactionType) AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByAccountNumberAndType(@Param("actNo") String actNo, @Param("transactionType") String transactionType, Pageable pageable);

    // Find all transactions for admin/staff roles
    @Query("SELECT t FROM Transaction t WHERE t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findAllTransactions(Pageable pageable);

    // Find all transactions for admin/staff roles, filtered by transaction type
    @Query("SELECT t FROM Transaction t WHERE (:transactionType IS NULL OR t.transactionType = :transactionType) AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findAllTransactionsByType(@Param("transactionType") String transactionType, Pageable pageable);

    // Find all transactions for all accounts belonging to a specific user
    @Query("SELECT t FROM Transaction t WHERE " +
           "(t.owner.userAccount.user = :user OR t.receiver.userAccount.user = :user) " +
           "AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByUser(User user, Pageable pageable);

    // Find all transactions for all accounts belonging to a specific user, filtered by transaction type
    @Query("SELECT t FROM Transaction t WHERE " +
           "(t.owner.userAccount.user = :user OR t.receiver.userAccount.user = :user) " +
           "AND (:transactionType IS NULL OR t.transactionType = :transactionType) " +
           "AND t.isDeleted = false ORDER BY t.transactionAt DESC")
    Page<Transaction> findTransactionHistoryByUserAndType(
           @Param("user") User user,
           @Param("transactionType") String transactionType,
           Pageable pageable);
}
