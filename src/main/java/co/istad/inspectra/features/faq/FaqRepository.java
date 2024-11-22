package co.istad.inspectra.features.faq;

import co.istad.inspectra.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq,Long> {

    Optional<Faq> findByAnswer(String answer);

    Optional<Faq> findByQuestion(String question);

    Optional<Faq> findByUuid(String category);


}
