package co.istad.inspectra.features.project.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectResponse(

        String uuid,
        String projectName,
        LocalDateTime createdAt,
        Boolean isDeleted,
        Boolean isUsed

) {
}
