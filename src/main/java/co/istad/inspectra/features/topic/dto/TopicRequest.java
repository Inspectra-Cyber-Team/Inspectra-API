package co.istad.inspectra.features.topic.dto;

import lombok.Builder;

@Builder
public record TopicRequest (
        String name
) {
}
