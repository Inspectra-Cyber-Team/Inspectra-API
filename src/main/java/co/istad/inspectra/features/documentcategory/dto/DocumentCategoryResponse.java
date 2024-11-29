package co.istad.inspectra.features.documentcategory.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DocumentCategoryResponse(

        String uuid,
        String name,
        String description,
        String createdAt,
        List<String> documentCategoryImages

) {
}
