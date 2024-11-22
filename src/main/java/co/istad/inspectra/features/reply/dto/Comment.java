package co.istad.inspectra.features.reply.dto;

import lombok.Builder;

@Builder
public record Comment(
        String uuid,
        String content
) {

}
