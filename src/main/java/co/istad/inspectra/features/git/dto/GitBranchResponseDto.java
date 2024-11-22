package co.istad.inspectra.features.git.dto;

import lombok.Builder;

@Builder
public record GitBranchResponseDto(

        String name

) {
}
