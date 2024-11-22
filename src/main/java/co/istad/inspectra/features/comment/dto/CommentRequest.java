package co.istad.inspectra.features.comment.dto;

import lombok.Builder;

@Builder
public record CommentRequest(

        String blogUuid,
        String userUuid,
        String content

) {
}
