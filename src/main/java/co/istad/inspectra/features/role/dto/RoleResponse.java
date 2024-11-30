package co.istad.inspectra.features.role.dto;

import lombok.Builder;

@Builder
public record RoleResponse
        (
                String uuid,
                String roleName
        )
{
}
