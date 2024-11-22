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
       * Get all comments
       * @see CommentServiceImpl#getAllComments
       * @return {@link  List<CommentResponse>}
       */

      List<CommentResponse> getAllComments();

      String likeComment(String commentUuid);

      void deleteLikeComment(String commentUuid);

}
