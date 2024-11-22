package co.istad.inspectra.repo;

import co.istad.inspectra.domain.DocumentImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentImageRepository extends JpaRepository<DocumentImages,Long> {


}
