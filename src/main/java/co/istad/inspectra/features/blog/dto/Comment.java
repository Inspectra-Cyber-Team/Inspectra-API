package co.istad.inspectra.features.blog.dto;

import lombok.Builder;

@Builder
public record Comment(
        String uuid,
        String content
) {

}
