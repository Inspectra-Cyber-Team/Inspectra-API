package co.istad.inspectra.features.comment;

import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;

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
      CommentResponse createComment(CommentRequest commentRequest);

      /**
       * get al comments by blog comment id
       * @see CommentServiceImpl#getCommentsByBlogUuid
       */

      CommentResponse getCommentsByBlogUuid(String blogUuid);

      /**
       * Get all comments
       * @see CommentServiceImpl#getAllComments
       * @return {@link  List<CommentResponse>}
       */

      List<CommentResponse> getAllComments();

      /**
       * Get a comment by uuid
       * @see CommentServiceImpl
       * @param commentUuid the comment uuid
       * @return {@link CommentResponse}
       */

      String likeComment(String commentUuid);

        /**
         * Delete a comment by uuid
         * @see CommentServiceImpl
         * @param commentUuid the comment uuid
         */

      void deleteLikeComment(String commentUuid);


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
         * @param content the comment request
         * @return {@link CommentResponse}
         */
        CommentResponse updateComment(String uuid, String content);
}
