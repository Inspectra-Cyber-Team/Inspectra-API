package co.istad.inspectra.features.userlikecomment;

import co.istad.inspectra.features.userlikecomment.dto.UserLikeCommentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserLikeCommentService {

    /**
     * Get all users who liked a comment
     * @param commentUUid comment uuid
     * @param page page number
     * @param size page size
     * @return page of users who liked the comment
     */
    Page<UserLikeCommentResponse> likeComment(String commentUUid, int page, int size);



}
