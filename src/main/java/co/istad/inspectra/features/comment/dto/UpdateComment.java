package co.istad.inspectra.features.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateComment(

        @NotBlank(message = "Comment content is required")
        String content
) {
}
