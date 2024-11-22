package co.istad.inspectra.features.documentcategory.dto;

import lombok.Builder;

@Builder
public record DocumentCategoryUpdate(
        String name,
        String description
) {
}
