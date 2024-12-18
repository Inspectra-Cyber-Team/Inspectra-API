package co.istad.inspectra.features.report.dto;

import lombok.Builder;

@Builder
public record ReportResponse(
        String uuid,
        String message,
        String name,
        String createdAt
) {
}
