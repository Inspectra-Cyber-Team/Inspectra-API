package co.istad.inspectra.features.project.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectOverview(Object project, List<Object> branches) {}