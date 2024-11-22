package co.istad.inspectra.features.faq.dto;

import lombok.Builder;

@Builder
public record FaqUpdateRequest(
        String question,
        String answer
) {
}
