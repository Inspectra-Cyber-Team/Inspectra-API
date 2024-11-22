package co.istad.inspectra.features.feedback;

import co.istad.inspectra.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Optional<Feedback> findByUuid(String uuid);


}
