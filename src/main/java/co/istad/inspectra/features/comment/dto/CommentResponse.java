package co.istad.inspectra.features.comment.dto;

import co.istad.inspectra.features.reply.dto.ReplyResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentResponse(

        String uuid,
        String content,
        int countLikes,
        UserDto user,
        List<ReplyResponse> replies,
        String createdAt


) {
}
