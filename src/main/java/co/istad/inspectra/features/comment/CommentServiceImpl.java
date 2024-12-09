package co.istad.inspectra.features.comment;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.domain.LikeComment;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.BlogRepository;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import co.istad.inspectra.features.comment.dto.UpdateComment;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.features.userlikecomment.UserLikeCommentRepository;
import co.istad.inspectra.mapper.CommentMapper;
import co.istad.inspectra.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static co.istad.inspectra.utils.WebSocketHandlerUtil.sessions;

@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    private final BlogRepository blogRepository;

    private final UserLikeCommentRepository userLikeCommentRepository;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String uuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(uuid);

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Blog blog = blogRepository.findByUuid(commentRequest.blogUuid()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        Comment comment = commentMapper.toCommentRequest(commentRequest);
        comment.setUuid(UUID.randomUUID().toString());
        comment.setUser(user);
        comment.setBlog(blog);
        comment.setCountComments(comment.getCountComments() + 1);
        commentRepository.save(comment);

        notifyClientNewComment(comment);

        return commentMapper.toCommentResponse(comment);
    }


    @Override
    public Page<CommentResponse> getAllComments(int page, int size) {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return commentRepository.findAll(pageRequest).map(commentMapper::toCommentResponse);

    }

    @Override
    public String likeComment(String commentUuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        Comment comment = commentRepository.findByUuid(commentUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        User user = userRepository.findUserByUuid(customUserDetails.getUserUuid());

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Optional<LikeComment> userLikeComment = userLikeCommentRepository.findByCommentUuidAndUserUuid(commentUuid, user.getUuid());

        if (userLikeComment.isPresent())
        {

            userLikeCommentRepository.delete(userLikeComment.get());

            comment.setCountLikes(comment.getCountLikes() - 1);

            commentRepository.save(comment);

            notifyClientNewComment(comment);

            return "Comment unliked";

        } else {

                LikeComment likeComment = new LikeComment();

                likeComment.setUuid(UUID.randomUUID().toString());

                likeComment.setUser(user);

                likeComment.setComment(comment);

                userLikeCommentRepository.save(likeComment);

                comment.setCountLikes(comment.getCountLikes() + 1);

                commentRepository.save(comment);

                notifyClientNewComment(comment);

                return "Comment liked";
        }


    }

    @Override
    public void deleteComment(String uuid) {

            Comment comment = commentRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

            notifyClientNewComment(comment);

            commentRepository.delete(comment);
    }

    @Override
    public CommentResponse updateComment(String uuid, UpdateComment updateComment) {

        Comment comment = commentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setContent(updateComment.content());

        commentRepository.save(comment);

        notifyClientNewComment(comment);

        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public Page<CommentResponse> getCommentsByBlogUuid(String blogUuid, int page, int size) {


        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }


        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));


        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page,size, sort);

        return commentRepository.findAllByBlogUuid(blogUuid, pageRequest).map(commentMapper::toCommentResponse);

    }

    private void notifyClientNewComment(Comment comment) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Serialize comment safely
            CommentResponse commentDto = commentMapper.toCommentResponse(comment);

            String json = objectMapper.writeValueAsString(commentDto);

            TextMessage webSocketMessage = new TextMessage(json);

            // Clean up closed sessions
            sessions.removeIf(session -> !session.isOpen());

            for (WebSocketSession session : sessions) {
                try {
                    session.sendMessage(webSocketMessage);
                } catch (IOException e) {
                    // Log error and skip this session
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send WebSocket message");
                }
            }

        } catch (Exception e) {
            // Log error and rethrow
            System.err.println("Error during WebSocket notification: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send WebSocket message");
        }
    }

}
