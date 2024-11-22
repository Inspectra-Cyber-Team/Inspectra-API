package co.istad.inspectra.features.issue.dto;

import java.util.List;

public record IssuesWrapperResponse(
        List<IssuesResponse> issues
) {}
