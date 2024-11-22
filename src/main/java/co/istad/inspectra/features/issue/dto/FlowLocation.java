package co.istad.inspectra.features.issue.dto;

import lombok.Builder;

@Builder
public record FlowLocation(
        String component,
        String msg,
        TextRange textRange
) {
}
