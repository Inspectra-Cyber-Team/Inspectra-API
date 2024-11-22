package co.istad.inspectra.features.faq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record FaqRequest(

        @NotBlank(message = "Question is required")
        @Size(min = 5, message = "Question must be at least 5 characters")
        String question,

        @NotBlank(message = "Answer is required")
        String answer
) {
}
