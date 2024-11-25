package co.istad.inspectra.features.project.dto;

import lombok.Builder;

@Builder
public record FavoritProjectReponse(
        String key,
        String name,
        String qualifier
) {
}
