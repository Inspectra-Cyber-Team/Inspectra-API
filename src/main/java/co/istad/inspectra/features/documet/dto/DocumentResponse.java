package co.istad.inspectra.features.documet.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record DocumentResponse(

        String uuid,
        String documentCategoryName,
        String title,
        String description,
        String createdAt,
        List<String> documentImages





) {
}
