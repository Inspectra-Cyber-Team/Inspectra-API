package co.istad.inspectra.features.comment.dto;

import lombok.Builder;

@Builder
public record CommentRequest(

        String blogUuid,
        String content

) {
}
