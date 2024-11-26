package co.istad.inspectra.features.report.dto;

import lombok.Builder;

@Builder
public record ReportResponseDetails(
        String uuid,
        String message,
        String createdAt,
        UserResponse user,
        BlogResponse blog
) {
}
