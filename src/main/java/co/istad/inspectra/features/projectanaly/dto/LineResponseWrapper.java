package co.istad.inspectra.features.projectanaly.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LineResponseWrapper(Component component) {

    public record Component(String key, String name, List<Measure> measures) {
    }

    public record Measure(String metric, String value) {
    }
}
