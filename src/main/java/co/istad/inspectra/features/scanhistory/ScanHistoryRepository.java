package co.istad.inspectra.features.scanhistory;

import co.istad.inspectra.domain.ScanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScanHistoryRepository extends JpaRepository<ScanHistory,Long> {

    List<ScanHistory> findByUserUuid(String uuid);



}
