package co.istad.inspectra.features.report;

import co.istad.inspectra.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ReportRepository extends JpaRepository<Report,Long> {

    Optional<Report> findByUuid(String uuid);

    Page<Report> findByBlogUuid(String blogUuid, PageRequest pageRequest);

}
