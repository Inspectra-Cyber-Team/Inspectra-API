package co.istad.inspectra.features.project.dto;

import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.user_role.dto.UserRoleResponse;
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
