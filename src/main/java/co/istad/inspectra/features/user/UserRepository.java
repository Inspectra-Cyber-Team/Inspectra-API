package co.istad.inspectra.features.user;

import co.istad.inspectra.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findUserByUuid(String uuid);
    Boolean existsByEmail(String email);
    Optional<User> findUsersByEmail(String email);

}
