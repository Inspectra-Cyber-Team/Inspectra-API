package co.istad.inspectra.features.comment;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.BlogRepository;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import co.istad.inspectra.features.comment.dto.UpdateComment;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.CommentMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Override
    public void deleteComment(String uuid) {

            Comment comment = commentRepository.findByUuid(uuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

            commentRepository.delete(comment);
    }

    @Override
    public CommentResponse updateComment(String uuid, UpdateComment updateComment) {

        Comment comment = commentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        comment.setContent(updateComment.content());

        commentRepository.save(comment);

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
}
