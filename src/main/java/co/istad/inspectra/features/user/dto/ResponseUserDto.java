package co.istad.inspectra.features.user.dto;

import co.istad.inspectra.features.user_role.dto.UserRoleResponse;
import lombok.Builder;
import java.time.LocalDateTime;
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
        Set<UserRoleResponse> roles
) {
}
