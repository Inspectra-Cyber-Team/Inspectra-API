package co.istad.inspectra.features.topic.dto;

import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record TopicResponseDetails(

        String uuid,
        String name,
        String createdAt,
        List<BlogResponseDto> blogs
) {
}
