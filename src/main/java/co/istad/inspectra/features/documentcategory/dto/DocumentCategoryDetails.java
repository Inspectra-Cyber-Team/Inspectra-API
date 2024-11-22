package co.istad.inspectra.features.documentcategory.dto;

import co.istad.inspectra.domain.Document;

import java.util.List;

public record DocumentCategoryDetails(
        String uuid,
        String name,
        String description,
        List<Document> documents,
        String createdBy,
        String updatedBy,
        String createdAt
        ){
}
