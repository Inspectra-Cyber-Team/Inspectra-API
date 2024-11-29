package co.istad.inspectra.features.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;


@Builder
public record ProjectOverViewResponse(
        @JsonProperty("component") Component component
) {}

@Builder
 record Component(
        String key,
        String name,
        String qualifier,
        List<MeasureDTO> measures
) {}

@Builder
  record MeasureDTO(
        String metric,
        String value,
        @JsonProperty("bestValue") boolean bestValue
) {}

@Builder
 record MetricValues(
        int INFO,
        int LOW,
        int MEDIUM,
        int HIGH,
        int BLOCKER,
        int total
) {}