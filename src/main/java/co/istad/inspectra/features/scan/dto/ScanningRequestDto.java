package co.istad.inspectra.features.scan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ScanningRequestDto(

        @NotBlank(message = "Git URL is required")
        @Pattern(regexp = "^(https?://)?(www\\.)?(github\\.com|gitlab\\.com)/.+$", message = "Only GitHub or GitLab URLs are allowed")
        String gitUrl,

        @NotBlank(message = "git branch is required")
        String branch,

        @NotBlank(message = "project name is required")
        String projectName



) {
}