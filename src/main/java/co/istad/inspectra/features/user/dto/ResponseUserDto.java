package co.istad.inspectra.features.user.dto;

import co.istad.inspectra.features.role.dto.RoleResponse;
import lombok.Builder;

import java.util.Set;

@Builder
public record ResponseUserDto(
        String uuid,
        String firstName,
        String lastName,
        String name,
        String email,
        String profile,
        String bio,
        String createdAt,

        Boolean isActive,

        String lastModifiedAt,
        Boolean isVerified,
        Boolean isDeleted,
        Set<RoleResponse> roles
) {
}
