package co.istad.inspectra.features.reply;

import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.domain.Reply;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.comment.CommentRepository;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import co.istad.inspectra.features.reply.dto.ReplyUpdate;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ReplyMapper;
import co.istad.inspectra.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static co.istad.inspectra.utils.WebSocketHandlerUtil.sessions;

@Service
@RequiredArgsConstructor

public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ReplyMapper replyMapper;

    @Override
    public ReplyResponse createReply(ReplyRequest request,@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String uuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(uuid);

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

        notifyClientNewReply(reply);

        return replyMapper.mapToReplyResponse(reply);

    }

    @Override
    public ReplyResponse getReplyByUuid(String replyUuid) {

        Reply reply = replyRepository.findByUuid(replyUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

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
    public String likeReply(String replyUuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String uuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(uuid);

        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Reply reply = replyRepository.findByUuid(replyUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        boolean isLiked = reply.getCountLikes() % 2 == 1;

        reply.setCountLikes(isLiked ? reply.getCountLikes() - 1 : reply.getCountLikes() + 1);

        replyRepository.save(reply);

        notifyClientNewReply(reply);

        return isLiked ? "Unliked" : "Liked";

    }

    @Override
    public void unlikeReply(String replyUuid) {

            Reply reply = replyRepository.findByUuid(replyUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

            reply.setCountLikes(reply.getCountLikes() - 1);

            replyRepository.save(reply);

            notifyClientNewReply(reply);
    }

    @Override
    public void deleteReply(String replyUuid) {

            Reply reply = replyRepository.findByUuid(replyUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

            replyRepository.delete(reply);

            notifyClientNewReply(reply);
    }

    @Override
    public ReplyResponse updateReply(String replyUuid, ReplyUpdate replyUpdate) {

        Reply reply = replyRepository.findByUuid(replyUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply not found"));

        reply.setContent(replyUpdate.content());

        notifyClientNewReply(reply);

       return replyMapper.mapToReplyResponse(replyRepository.save(reply));
    }

    private void notifyClientNewReply(Reply reply) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {

            ReplyResponse replyResponse = replyMapper.mapToReplyResponse(reply);

            String replyJson = objectMapper.writeValueAsString(replyResponse);

            // send notification to client
            TextMessage message = new TextMessage(replyJson);

            sessions.removeIf(session -> !session.isOpen());

            for (WebSocketSession session: sessions){

                try{
                    session.sendMessage(message);

                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending notification");
                }
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error sending notification");
        }

    }
}
