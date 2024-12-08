package co.istad.inspectra.features.reply;

import co.istad.inspectra.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findByParentCommentUuid(String commentUuid);

    Optional<Reply> findByUuidAndUserUuid(String uuid, String userUuid);

    Optional<Reply> findByUuid(String uuid);

}
