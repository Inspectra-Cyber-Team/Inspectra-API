package co.istad.inspectra.features.user.dto;

import lombok.Builder;

@Builder
public record UpdateUserDto(
        String name,
        String profile,
        String bio
) {
}
