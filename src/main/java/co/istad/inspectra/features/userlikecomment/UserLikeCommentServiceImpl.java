package co.istad.inspectra.features.userlikecomment;

import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.features.comment.CommentRepository;
import co.istad.inspectra.features.userlikecomment.dto.UserLikeCommentResponse;
import co.istad.inspectra.mapper.UserLikeCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserLikeCommentServiceImpl implements UserLikeCommentService {

    private final UserLikeCommentRepository userLikeCommentRepository;

    private final CommentRepository commentRepository;

    private final UserLikeCommentMapper userLikeCommentMapper;
    @Override
    public Page<UserLikeCommentResponse> likeComment(String commentUUid,int page, int size) {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");
        }

        Comment comment = commentRepository.findByUuid(commentUUid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return userLikeCommentRepository.findByCommentUuid(commentUUid, pageRequest)
                .map(userLikeCommentMapper::toUserLikeCommentResponse);

    }
}
