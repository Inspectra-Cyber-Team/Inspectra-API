package co.istad.inspectra.features.issue.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record IssuesResponse(

        String key,
        String rule,
        String severity,
        String component,
        String project,
        int line,
        String message,
        String status,
        String creationDate,
        String updateDate,
        String scope,
        String resolution,
        TextRange textRange,
        List<Flows> flows



) {
}
