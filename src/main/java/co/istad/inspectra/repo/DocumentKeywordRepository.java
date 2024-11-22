package co.istad.inspectra.repo;

import co.istad.inspectra.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentKeywordRepository extends JpaRepository<Keyword,Long> {


}
