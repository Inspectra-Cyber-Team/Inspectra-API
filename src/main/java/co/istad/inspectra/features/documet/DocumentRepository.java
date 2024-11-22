package co.istad.inspectra.features.documet;

import co.istad.inspectra.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

  Optional<Document> findByUuid(String uuid);

  Optional<Document> findByCategoryName(String title);


}
