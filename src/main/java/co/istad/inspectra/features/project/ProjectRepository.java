package co.istad.inspectra.features.project;

import co.istad.inspectra.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Boolean existsByProjectName(String projectName);

    Optional<Project> findByProjectName(String projectName);

    List<Project> findByUserUuid(String userUuid);

    @Transactional
    void deleteProjectByProjectName(String projectName);

}
