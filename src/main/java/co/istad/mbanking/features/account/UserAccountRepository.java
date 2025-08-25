package co.istad.mbanking.features.account;

import co.istad.mbanking.domain.Account;
import co.istad.mbanking.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    @Query("SELECT ua.account FROM UserAccount ua WHERE ua.user.uuid = :userUuid AND ua.isDeleted = false")
    List<Account> findAccountsByUserUuid(@Param("userUuid") String userUuid);

    @Query("SELECT ua.account FROM UserAccount ua WHERE ua.user.uuid = :userUuid AND ua.isDeleted = false AND ua.account.isDeleted = false")
    List<Account> findActiveAccountsByUserUuid(@Param("userUuid") String userUuid);
}
