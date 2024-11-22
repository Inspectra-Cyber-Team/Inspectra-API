package co.istad.inspectra.features.issue.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record Flows(
        List<FlowLocation> locations
) {
}
