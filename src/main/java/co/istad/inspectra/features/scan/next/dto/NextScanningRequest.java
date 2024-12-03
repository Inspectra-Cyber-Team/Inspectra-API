package co.istad.inspectra.features.scan.next.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

@Builder
public record NextScanningRequest(

        @NotBlank(message = "Git URL is required")
        @Pattern(regexp = "^(https?://)?(www\\.)?(github\\.com|gitlab\\.com)/.+$", message = "Only GitHub or GitLab URLs are allowed")
        String gitUrl,

        @NotBlank(message = "git branch is required")
        String branch,

        @NotBlank(message = "project name is required")
        String projectName,

        List<String> issueTypes,
        List<String> excludePaths

) {
}
