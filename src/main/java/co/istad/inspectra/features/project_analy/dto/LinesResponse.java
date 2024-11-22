package co.istad.inspectra.features.project_analy.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LinesResponse(

        String ncloc,
        String nclocLanguageDistribution


) {
}
