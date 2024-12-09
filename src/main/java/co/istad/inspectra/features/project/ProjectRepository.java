package co.istad.inspectra.features.project;

import co.istad.inspectra.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Boolean existsByProjectName(String projectName);

    @Query("SELECT p FROM Project p WHERE p.projectName = :projectName AND p.isDeleted = false")
    Optional<Project> findByProjectName(String projectName);

    @Query("SELECT p FROM Project p WHERE p.user.uuid = :userUuid AND p.isDeleted = false")
    List<Project> findByUserUuid(String userUuid);

    @Query("SELECT p FROM Project p WHERE p.user.uuid = :userUuid AND p.isDeleted = false")
    Page<Project> findByUserUuid(String userUuid, Pageable pageable);





}
