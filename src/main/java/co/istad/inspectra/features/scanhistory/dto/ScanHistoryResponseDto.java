package co.istad.inspectra.features.scanhistory.dto;

import lombok.Builder;

@Builder
public record ScanHistoryResponseDto(
        String uuid,
        String projectName,
        String userUuid,
        String userFullName

) {
}
