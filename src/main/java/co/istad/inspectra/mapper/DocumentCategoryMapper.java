package co.istad.inspectra.mapper;


import co.istad.inspectra.domain.DocumentCategory;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryDetails;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryRequest;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface DocumentCategoryMapper {

    DocumentCategory mapToDocumentCategory(DocumentCategoryRequest documentCategoryRequest);

    DocumentCategoryResponse mapToDocumentCategoryResponse(DocumentCategory documentCategory);

    @Mapping(qualifiedByName = "mapToDocumentResponse", target = "documents")
    DocumentCategoryDetails mapToDocumentCategoryDetails(DocumentCategory documentCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateDocumentCategory(@MappingTarget DocumentCategory documentCategory, DocumentCategoryUpdate documentCategoryUpdate);

}
