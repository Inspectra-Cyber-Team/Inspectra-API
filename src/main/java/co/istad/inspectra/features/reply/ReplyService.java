package co.istad.inspectra.features.reply;

import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;

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
    ReplyResponse createReply(ReplyRequest request);

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

}
