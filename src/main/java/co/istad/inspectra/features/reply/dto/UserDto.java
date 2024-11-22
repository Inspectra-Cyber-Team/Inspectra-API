package co.istad.inspectra.features.reply.dto;

import lombok.Builder;

@Builder
public record UserDto(
    String uuid,
    String firstName,
    String lastName,
    String profile,
    String bio
) {}