package co.istad.inspectra.features.feedback.dto;


import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record FeedbackResponse(

        String uuid,
        String firstName,
        String lastName,
        String email,
        String profile,
        String message,
        Timestamp createdAt

) {
}
