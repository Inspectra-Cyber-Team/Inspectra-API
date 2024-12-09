package co.istad.inspectra.features.userlikecomment;

import co.istad.inspectra.domain.LikeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLikeCommentRepository extends JpaRepository<LikeComment,Long> {

    Optional<LikeComment> findByCommentUuidAndUserUuid(String uuid, String userUuid);

    Page<LikeComment> findByCommentUuid(String commentUuid, PageRequest pageRequest);

}
