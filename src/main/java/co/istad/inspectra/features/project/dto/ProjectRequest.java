package co.istad.inspectra.features.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ProjectRequest(

        @NotBlank(message = "Project name is required")
        @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Project name must not contain whitespace")
        String projectName
) {
}
