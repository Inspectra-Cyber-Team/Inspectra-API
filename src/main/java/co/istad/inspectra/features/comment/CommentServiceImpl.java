package co.istad.inspectra.features.comment;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.BlogRepository;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final BlogRepository blogRepository;

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {

        User user = userRepository.findUserByUuid(commentRequest.userUuid());

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


        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public List<CommentResponse> getAllComments() {

        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentResponse)
                .toList();

    }

    @Override
    public String likeComment(String commentUuid) {

        Comment comment = commentRepository.findByUuid(commentUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setCountLikes(comment.getCountLikes() + 1);

        commentRepository.save(comment);

        return "Comment liked successfully";

    }

    @Override
    public void deleteLikeComment(String commentUuid) {

        Comment comment = commentRepository.findByUuid(commentUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setCountLikes(comment.getCountLikes() - 1);

        commentRepository.save(comment);

    }
}
