package co.istad.inspectra.features.report.dto;

import lombok.Builder;

@Builder
public record BlogResponse(
        String uuid,
        String title,
        String description
) {
}
