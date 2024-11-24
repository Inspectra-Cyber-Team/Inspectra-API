package co.istad.inspectra.features.blog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BlogUpdateRequest(
        String title,
        String description,
        List<String> thumbnail
) {
}
