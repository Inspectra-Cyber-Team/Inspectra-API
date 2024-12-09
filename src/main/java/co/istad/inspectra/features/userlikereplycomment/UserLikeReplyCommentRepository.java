package co.istad.inspectra.features.userlikereplycomment;

import co.istad.inspectra.domain.LikeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikeReplyCommentRepository extends JpaRepository<LikeReply,Long> {

    Optional<LikeReply> findByReplyUuidAndUserUuid(String replyUuid, String userUuid);
    Page<LikeReply> findByReplyUuid(String replyUuid, PageRequest pageRequest);

}
