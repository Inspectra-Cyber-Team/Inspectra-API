package co.istad.inspectra.features.documentcategory.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DocumentCategoryUpdate(
        String name,
        String description,
        List<String> documentCategoryImages
) {
}
