package co.istad.inspectra.features.projectanaly.dto;

import lombok.Builder;

@Builder
public record LinesResponse(

        String ncloc,
        String nclocLanguageDistribution


) {
}
