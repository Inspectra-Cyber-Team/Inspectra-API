package co.istad.inspectra.features.faq.dto;

import lombok.Builder;

@Builder
public record FaqResponse(

        String uuid,
        String question,
        String answer,
        String createdAt

) {
}
