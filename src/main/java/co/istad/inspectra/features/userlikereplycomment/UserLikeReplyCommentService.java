package co.istad.inspectra.features.userlikereplycomment;

import co.istad.inspectra.features.userlikereplycomment.dto.UserLikeReplyCommentResponse;
import org.springframework.data.domain.Page;

/**
 * User like reply comment service
 * This service is responsible for handling user like reply comment related operations
 * such as getting all users who liked a reply comment
 * @see UserLikeReplyCommentServiceImpl
 */
public interface UserLikeReplyCommentService {

    /**
     * Get all users who liked a reply comment
     * @param replyCommentUuid reply comment uuid
     * @param page page number
     * @param size page size
     * @return page of users who liked the reply comment
     */
    Page<UserLikeReplyCommentResponse> likeReplyComment(String replyCommentUuid, int page, int size);

}
