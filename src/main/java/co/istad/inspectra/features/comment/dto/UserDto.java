package co.istad.inspectra.features.comment.dto;

import lombok.Builder;

@Builder
public record UserDto(
    String uuid,
    String firstName,
    String lastName,
    String email,
    String profile,
    String bio
) {}