package co.istad.inspectra.features.user;

import co.istad.inspectra.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    @Query("SELECT u FROM User u WHERE u.uuid = :uuid AND u.isDeleted = false")
    User findUserByUuid(String uuid);

    Boolean existsByEmail(String email);
    Boolean existsByName(String userName);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findUsersByEmail(String email);


    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.isDeleted = false AND r.roleName = 'ROLE_ADMIN'")
    Page<User> findAllAdmin(PageRequest pageRequest);



}
