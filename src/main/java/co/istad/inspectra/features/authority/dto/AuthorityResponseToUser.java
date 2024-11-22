package co.istad.inspectra.features.authority.dto;

import lombok.Builder;

@Builder
public record AuthorityResponseToUser(
        String authorityName
) {
}
