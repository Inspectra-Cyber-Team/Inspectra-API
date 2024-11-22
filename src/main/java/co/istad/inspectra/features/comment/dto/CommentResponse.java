package co.istad.inspectra.features.comment.dto;

import lombok.Builder;

@Builder
public record CommentResponse(

        String uuid,
        String content,
        int countLikes,
        UserDto user,
        String createdAt


) {
}
