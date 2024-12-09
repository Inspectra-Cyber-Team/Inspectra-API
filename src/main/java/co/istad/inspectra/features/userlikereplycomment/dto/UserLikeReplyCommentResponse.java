package co.istad.inspectra.features.userlikereplycomment.dto;

import lombok.Builder;

@Builder
public record UserLikeReplyCommentResponse(
        String userUuid,
        String firstName,
        String lastName,
        String profile,
        String email,
        String bio
) {
}
