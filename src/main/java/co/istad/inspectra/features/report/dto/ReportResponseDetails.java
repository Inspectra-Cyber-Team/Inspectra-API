package co.istad.inspectra.features.report.dto;

import lombok.Builder;

@Builder
public record ReportResponseDetails(
        String uuid,
        String blogUuid,
        String message,
        String createdAt,
        UserResponse user
) {
}
