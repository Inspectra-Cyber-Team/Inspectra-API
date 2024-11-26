package co.istad.inspectra.features.userrole.dto;

import lombok.Builder;

@Builder
public record UserRoleResponse
        (
                String uuid,
                String roleName
        )
{
}
