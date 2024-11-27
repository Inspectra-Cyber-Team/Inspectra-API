package co.istad.inspectra.features.userlikeblog.dto;

import lombok.Builder;

@Builder
public record UserLikeBlogResponse(
        String uuid,
        String userUuid,
        String firstName,
        String lastName,
        String profile,
        String bio
) {
}
