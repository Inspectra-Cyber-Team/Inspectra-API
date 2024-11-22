package co.istad.inspectra.features.user_role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRoleRequest(

        @NotBlank(message = "Role name is required")
        String roleName
) {
}
