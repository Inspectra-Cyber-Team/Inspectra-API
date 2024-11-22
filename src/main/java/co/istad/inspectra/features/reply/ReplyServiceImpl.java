package co.istad.inspectra.features.reply;

import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.domain.Reply;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.comment.CommentRepository;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ReplyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ReplyMapper replyMapper;

    @Override
    public ReplyResponse createReply(ReplyRequest request) {

        User user = userRepository.findUserByUuid(request.userUuid());

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Comment comment = commentRepository.findByUuid(request.commentUuid()).

                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        Reply reply = replyMapper.mapToReplyRequest(request);
        reply.setUuid(UUID.randomUUID().toString());
        reply.setUser(user);
        reply.setParentComment(comment);

        replyRepository.save(reply);

        return replyMapper.mapToReplyResponse(reply);

    }

    @Override
    public List<ReplyResponse> getAllReplies() {

        return replyRepository.findAll().stream()
                .map(replyMapper::mapToReplyResponse)
                .toList();
    }

    @Override
    public List<ReplyResponse> getReplyByCommentId(String commentId) {

        List<Reply> replies = replyRepository.findByParentCommentUuid(commentId);

        if(replies.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Replies not found");
        }

        return replyMapper.mapToReplyResponse(replies);

    }

    @Override
    public String likeReply(String replyUuid) {

        Reply reply = replyRepository.findByUuid(replyUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        reply.setCountLikes(reply.getCountLikes() + 1);

        replyRepository.save(reply);


        return "Liked";
    }

    @Override
    public void unlikeReply(String replyUuid) {

            Reply reply = replyRepository.findByUuid(replyUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

            reply.setCountLikes(reply.getCountLikes() - 1);

            replyRepository.save(reply);
    }
}
