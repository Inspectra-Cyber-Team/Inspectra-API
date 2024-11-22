package co.istad.inspectra.features.issue.dto;

import lombok.Builder;

@Builder
public record TextRange(
        int startLine,
        int endLine,
        int startOffset,
        int endOffset
)
{ }
