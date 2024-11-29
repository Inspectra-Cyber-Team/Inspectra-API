package co.istad.inspectra.features.anonymoususer;

import co.istad.inspectra.domain.AnonymousUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnonymousUserRepository extends JpaRepository<AnonymousUser,Long> {

}
