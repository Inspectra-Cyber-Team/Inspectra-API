package co.istad.inspectra.features.authority;
import co.istad.inspectra.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthorityName(String authorityName);
    Set<Authority> findAllByAuthorityName(String authorityName);

    Optional<Authority> findByUuid(String uuid);
    boolean existsByAuthorityName(String authorityName);
}
