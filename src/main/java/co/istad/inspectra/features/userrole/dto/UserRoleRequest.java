package co.istad.inspectra.features.userrole.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRoleRequest(

        @NotBlank(message = "Role name is required")
        String roleName
) {
}
