package co.istad.inspectra.features.comment;

import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import co.istad.inspectra.features.comment.dto.UpdateComment;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;


/**
 * Service for managing comments
 * @see CommentServiceImpl
 * @author lyhou
 * @version 1.0
 */
public interface CommentService {

      /**
       * Create a comment
       * @see CommentServiceImpl#createComment
       * @param commentRequest the comment request
       * @return {@link CommentResponse}
       */
      CommentResponse createComment(CommentRequest commentRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails);


      /**
       * Get all comments
       * @see CommentServiceImpl#getAllComments
       * @return {@link  List<CommentResponse>}
       */

      Page<CommentResponse> getAllComments(int page, int size);

      /**
       * Get a comment by uuid
       * @see CommentServiceImpl
       * @param commentUuid the comment uuid
       * @return {@link CommentResponse}
       */

      String likeComment(String commentUuid,@AuthenticationPrincipal CustomUserDetails customUserDetails);


      /**
       * Delete a comment by uuid
       * @see CommentServiceImpl
       * @param uuid the comment uuid
       */
      void deleteComment(String uuid);

        /**
         * Update a comment by uuid
         * @see CommentServiceImpl
         * @param uuid the comment uuid
         * @param updateComment the comment request
         * @return {@link CommentResponse}
         */
        CommentResponse updateComment(String uuid, UpdateComment updateComment);


    /**
     * Get all comments by blog uuid
     * @see CommentServiceImpl
     * @param blogUuid the blog uuid
     *
     */
    Page<CommentResponse> getCommentsByBlogUuid(String blogUuid, int page, int size);
}
