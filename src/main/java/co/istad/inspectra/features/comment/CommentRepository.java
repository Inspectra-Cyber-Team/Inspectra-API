package co.istad.inspectra.features.comment;

import co.istad.inspectra.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUuid(String uuid);

    Page<Comment> findAllByBlogUuid(String blogUuid, PageRequest pageRequest);

}
