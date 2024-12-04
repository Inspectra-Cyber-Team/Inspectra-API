package co.istad.inspectra.features.reply;

import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import co.istad.inspectra.features.reply.dto.ReplyUpdate;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

/*
    * Service for managing replies
    * @see ReplyServiceImpl
    * @author lyhou
    * @version 1.0
 */
public interface ReplyService {

    /**
     * Create a reply
     * @see ReplyServiceImpl#createReply
     * @param request the reply request
     * @return {@link ReplyResponse}
     */
    ReplyResponse createReply(ReplyRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    /**
     * Get a reply by uuid
     * @see ReplyServiceImpl#getReplyByUuid
     * @param replyUuid the reply id
     *                  @return {@link ReplyResponse}
     */

    ReplyResponse getReplyByUuid(String replyUuid);


    /**
     * Get all replies
     * @see ReplyServiceImpl#getAllReplies
     * @return {@link ReplyResponse}
     *
     */

    List<ReplyResponse> getAllReplies();

    /**
     * Get a reply by id
     * @see ReplyServiceImpl
     * @param commentId the reply id
     * @return {@link ReplyResponse}
     */
    List<ReplyResponse> getReplyByCommentId(String commentId);


    /**
     * Like a reply comment
     * @see ReplyServiceImpl#likeReply
     * @param replyUuid the reply id
     */

    String likeReply(String replyUuid);

    /**
     * Unlike a reply comment
     * @see ReplyServiceImpl#unlikeReply
     * @param replyUuid the reply id
     */
    void unlikeReply(String replyUuid);

    /**
     * delete a reply comment
     * @see ReplyServiceImpl#deleteReply
     * @param replyUuid the reply id
     *
     *
     */
    void deleteReply(String replyUuid);


    ReplyResponse updateReply(String replyUuid, ReplyUpdate replyUpdate);

}
