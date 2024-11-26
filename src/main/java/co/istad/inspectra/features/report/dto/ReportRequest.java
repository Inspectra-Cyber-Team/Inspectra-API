package co.istad.inspectra.features.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReportRequest(
        @NotBlank(message = "blogUuid is required")
        String blogUuid,

       @NotBlank(message = "message to report blog is required")
       String message
) {
}
