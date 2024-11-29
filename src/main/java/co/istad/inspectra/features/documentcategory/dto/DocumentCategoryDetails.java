package co.istad.inspectra.features.documentcategory.dto;

import co.istad.inspectra.features.documet.dto.DocumentResponse;

import java.util.List;

public record DocumentCategoryDetails(
        String uuid,
        String name,
        String description,
        List<DocumentResponse> documents,
        String createdBy,
        String updatedBy,
        String createdAt
        ){
}
