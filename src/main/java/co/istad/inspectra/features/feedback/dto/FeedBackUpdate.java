package co.istad.inspectra.features.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FeedBackUpdate(
        @NotBlank(message = "Feedback message is required")
        String message
) {
}
