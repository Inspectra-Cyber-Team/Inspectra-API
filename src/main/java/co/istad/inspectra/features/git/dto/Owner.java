package co.istad.inspectra.features.git.dto;

import lombok.Builder;

@Builder
public record Owner(
        String login,
        String id,
        String avatar_url,
        String html_url
) {
}
