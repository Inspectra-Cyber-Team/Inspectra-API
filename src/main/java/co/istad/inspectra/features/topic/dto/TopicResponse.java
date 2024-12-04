package co.istad.inspectra.features.topic.dto;

import lombok.Builder;

@Builder
public record TopicResponse(
        String uuid,
        String name,
        String createdAt
) {
}
