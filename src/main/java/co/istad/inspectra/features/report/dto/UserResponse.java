package co.istad.inspectra.features.report.dto;

import lombok.Builder;

@Builder
public record UserResponse (
        String uuid,
        String email,
        String name,
        String profile
) {


}
