package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Document;
import co.istad.inspectra.domain.DocumentImages;
import co.istad.inspectra.domain.Keyword;
import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document mapToDocument(DocumentRequest documentRequest);

    // Custom mappings for images and keywords
    @Named(("mapToDocumentResponse"))
    @Mapping(target = "documentCategoryName", source = "category.name")
    @Mapping(target = "documentImages", source = "images", qualifiedByName = "mapImagesToUrls")
    DocumentResponse mapToDocumentResponse(Document document);



    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateDocumentFromRequest(@MappingTarget  Document document, DocumentUpdate documentUpdate);


    @Named("mapImagesToUrls")
    default List<String> mapImagesToUrls(List<DocumentImages> images) {
        return images != null ? images.stream()
                .map(DocumentImages::getThumbnail)
                .collect(Collectors.toList()) : null;
    }




}
