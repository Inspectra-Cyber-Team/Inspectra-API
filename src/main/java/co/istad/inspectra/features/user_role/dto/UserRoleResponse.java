package co.istad.inspectra.features.user_role.dto;

import lombok.Builder;

@Builder
public record UserRoleResponse
        (
                String uuid,
                String roleName
        )
{
}
