package co.istad.inspectra.features.scan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;

@Builder
public record ScanForNonUserRequest (
        @NotBlank(message = "Git URL is required")
        @Pattern(regexp = "^(https?://)?(www\\.)?(github\\.com|gitlab\\.com)/.+$", message = "Only GitHub or GitLab URLs are allowed")
        String gitUrl,

        @NotBlank(message = "git branch is required")
        String branch,

        @Positive(message = "Scan count must be greater than 0")
        @NotNull(message = "Scan count is required")
        int countScan,
        List<String> issueTypes,
        List<String> includePaths

) {
}
