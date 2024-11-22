package co.istad.inspectra.features.scanning.test.dto;

import lombok.Builder;

@Builder
public record AnalyzeRequest(
        String repoUrl,
        String branch,
        String projectName
) {
}
