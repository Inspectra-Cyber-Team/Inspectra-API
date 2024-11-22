package co.istad.inspectra.features.feedback.dto;

import co.istad.inspectra.features.user.dto.ResponseUserDto;

public record FeedbackResponseDetails(
        String uuid,
        String name,
        String email,
        String profile,
        String message,
        String createdAt,
        ResponseUserDto user

) {
}
