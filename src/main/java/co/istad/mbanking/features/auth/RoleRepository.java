package co.istad.mbanking.features.auth;

import co.istad.mbanking.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
