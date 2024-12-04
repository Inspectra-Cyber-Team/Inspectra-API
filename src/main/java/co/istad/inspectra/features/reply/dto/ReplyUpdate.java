package co.istad.inspectra.features.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReplyUpdate (

        @NotBlank(message = "Reply content is required")
        String content
) {
}
