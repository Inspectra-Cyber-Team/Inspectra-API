package co.istad.inspectra.features.authority.dto;


import lombok.Builder;

@Builder
public record AuthorityResponse(

        String uuid,
        String authorityName,
        String description
) {
}
