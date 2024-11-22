package co.istad.inspectra.features.documentcategory.dto;

import lombok.Builder;

@Builder
public record DocumentCategoryResponse(

        String uuid,
        String name,
        String description,
        String createdAt

) {
}
