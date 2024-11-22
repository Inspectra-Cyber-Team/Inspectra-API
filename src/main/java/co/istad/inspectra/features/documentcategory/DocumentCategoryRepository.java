package co.istad.inspectra.features.documentcategory;

import co.istad.inspectra.domain.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory,Long> {

    Optional<DocumentCategory> findByName(String name);

    Optional<DocumentCategory> findByUuid(String uuid);

}
