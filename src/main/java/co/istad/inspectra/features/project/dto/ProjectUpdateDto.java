package co.istad.inspectra.features.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProjectUpdateDto(

        @NotBlank(message = "Project name is required")
        String projectName,

        @NotBlank(message = "New project name is required")
        String newProjectName
) {
}
