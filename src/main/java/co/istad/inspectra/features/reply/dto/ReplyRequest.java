package co.istad.inspectra.features.reply.dto;

import lombok.Builder;

@Builder
public record ReplyRequest(

        String commentUuid,
        String content

) {
}
