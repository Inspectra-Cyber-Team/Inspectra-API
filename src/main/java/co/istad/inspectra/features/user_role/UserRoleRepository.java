package co.istad.inspectra.features.user_role;

import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByRoleName(EnumRole roleName);

    Optional<Role> findRoleByUuid(String uuid);



}
