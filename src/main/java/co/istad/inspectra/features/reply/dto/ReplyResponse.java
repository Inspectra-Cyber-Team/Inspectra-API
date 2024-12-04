package co.istad.inspectra.features.reply.dto;

import lombok.Builder;

@Builder
public record ReplyResponse(

        String uuid,
        String content,
        String createdAt,
        int countLikes,
        UserDto user

) {
}
