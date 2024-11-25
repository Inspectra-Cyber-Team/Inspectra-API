package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Document;
import co.istad.inspectra.domain.DocumentImages;
import co.istad.inspectra.domain.Keyword;
import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document mapToDocument(DocumentRequest documentRequest);

    // Custom mappings for images and keywords
    @Mapping(target = "documentCategoryName", source = "category.name")
    @Mapping(target = "documentImages", source = "images")
    DocumentResponse mapToDocumentResponse(Document document);



    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateDocumentFromRequest( DocumentUpdate documentUpdate,@MappingTarget  Document document);


    default List<String> mapImagesToUrls(List<DocumentImages> images) {
        return images != null ? images.stream()
                .map(DocumentImages::getThumbnail)
                .collect(Collectors.toList()) : null;
    }




}
