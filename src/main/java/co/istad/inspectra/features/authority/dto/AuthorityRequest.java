package co.istad.inspectra.features.authority.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthorityRequest(

        @NotBlank(message = "Authority name is required")
        String authorityName,
        String description
) {
}
