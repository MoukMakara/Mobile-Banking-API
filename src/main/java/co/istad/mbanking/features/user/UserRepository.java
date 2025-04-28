package co.istad.mbanking.features.user;

import co.istad.mbanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT * FROM users WHERE phone_number = ?
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    // Derived Query Method
    Boolean existsByNationalCardId(String nationalCardId);

    // JPQL = Jakarta Persistent Query Language
    // @Query(value = "SELECT EXISTS (SELECT * FROM users WHERE national_card_id = ?1)", nativeQuery = true)
    @Query("""
                SELECT EXISTS (SELECT u FROM
                User AS u
                WHERE u.nationalCardId = ?1 AND u.isDeleted = FALSE)
            """)
    Boolean isNationalCardIdExisted(String nationalCardId);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Optional<User> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

}
