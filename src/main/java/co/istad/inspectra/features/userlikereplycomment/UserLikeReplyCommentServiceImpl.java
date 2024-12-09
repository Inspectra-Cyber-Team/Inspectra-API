package co.istad.inspectra.features.userlikereplycomment;

import co.istad.inspectra.domain.Reply;
import co.istad.inspectra.features.reply.ReplyRepository;
import co.istad.inspectra.features.userlikereplycomment.dto.UserLikeReplyCommentResponse;
import co.istad.inspectra.mapper.UserLikeReplyCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor

public class UserLikeReplyCommentServiceImpl implements UserLikeReplyCommentService {

    private final UserLikeReplyCommentRepository userLikeReplyCommentRepository;

    private final UserLikeReplyCommentMapper userLikeReplyCommentMapper;

    private final ReplyRepository repository;

    @Override
    public Page<UserLikeReplyCommentResponse> likeReplyComment(String replyCommentUuid, int page, int size) {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");
        }

        Reply reply = repository.findByUuid(replyCommentUuid)

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));


        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return userLikeReplyCommentRepository.findByReplyUuid(replyCommentUuid, pageRequest)

                .map(userLikeReplyCommentMapper::toUserLikeCommentResponse);
    }

}
