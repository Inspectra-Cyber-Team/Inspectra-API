package co.istad.inspectra.features.topic;

import co.istad.inspectra.domain.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {

    Boolean existsByName(String name);

    Optional<Topic> findByName(String name);

    Optional<Topic> findByUuid(String uuid);

    Page<Topic> findByName(String name, PageRequest pageRequest);



}
