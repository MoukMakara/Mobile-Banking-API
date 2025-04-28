package co.istad.mbanking.features.auth;

import co.istad.mbanking.domain.EmailVerification;
import co.istad.mbanking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {

    Optional<EmailVerification> findByUser(User user);

}
