package co.istad.inspectra.features.userlikecomment.dto;

import lombok.Builder;

@Builder
public record UserLikeCommentResponse(
        String uuid,
        String userUuid,
        String firstName,
        String lastName,
        String email,
        String profile,
        String bio
) {
}
