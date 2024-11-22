package co.istad.inspectra.features.documet;


import co.istad.inspectra.domain.Document;
import co.istad.inspectra.domain.DocumentImages;
import co.istad.inspectra.domain.Keyword;
import co.istad.inspectra.features.documentcategory.DocumentCategoryRepository;
import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
import co.istad.inspectra.mapper.DocumentMapper;
import co.istad.inspectra.repo.DocumentImageRepository;
import co.istad.inspectra.repo.DocumentKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentCategoryRepository documentCategoryRepository;

    private final DocumentImageRepository documentImageRepository;

    private final DocumentKeywordRepository documentKeywordRepository;

    @Override
    public DocumentResponse createDocument(DocumentRequest documentRequest) {

        var documentCategory = documentCategoryRepository.findByName(documentRequest.documentCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document category not found with name: "
                        + documentRequest.documentCategoryName()));

        var document = documentMapper.mapToDocument(documentRequest);

        document.setUuid(UUID.randomUUID().toString());
        document.setCategory(documentCategory);

        documentRepository.save(document);

         List<DocumentImages> images = documentRequest.documentImagesRequest().stream()
                .map(image -> {
                    var documentImage = new DocumentImages();
                    documentImage.setUuid(UUID.randomUUID().toString());
                    documentImage.setDocument(document);
                    documentImage.setThumbnail(image);

                    documentImageRepository.save(documentImage);

                    return documentImage;
                }).toList();

        document.setImages(images);

        List<Keyword> keywords = documentRequest.documentKeywordRequest().stream()
                .map(keyword -> {
                    var documentKeyword = new Keyword();
                    documentKeyword.setUuid(UUID.randomUUID().toString());
                    documentKeyword.setDocuments(new ArrayList<>());
                    documentKeyword.setKeyword(keyword);

                    documentKeywordRepository.save(documentKeyword);

                    return documentKeyword;
                }).toList();

        document.setKeywords(keywords);


        return documentMapper.mapToDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getAllDocument() {

        return documentRepository.findAll().stream()

                .map(documentMapper::mapToDocumentResponse)

                .toList();

    }

    @Override
    public DocumentResponse getDocumentByUuid(String uuid) {

        return documentRepository.findByUuid(uuid)

                .map(documentMapper::mapToDocumentResponse)

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found with uuid: " + uuid));

    }

    @Override
    public DocumentResponse updateDocument(String uuid, DocumentUpdate documentUpdate) {
        // Fetch the existing document from the repository
        Document document = documentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found with uuid: " + uuid));

        // Update basic fields of the document
        document.setTitle(documentUpdate.title());
        document.setDescription(documentUpdate.documentDescription());

        // Update the category if necessary
//        if (documentUpdate.documentCategoryName() != null) {
//            var documentCategory = documentCategoryRepository.findByName(documentUpdate.documentCategoryName())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document category not found with name: "
//                            + documentUpdate.documentCategoryName()));
//            document.setCategory(documentCategory);
//        }

        // Handle images update: delete orphaned images and add new ones
        if (documentUpdate.documentImagesRequest() != null) {
            // Remove all existing images from the document
            document.getImages().clear();  // Remove the references from the Document entity
            // No need to manually delete them as orphanRemoval = true will take care of it

            // Add new images
            List<DocumentImages> images = documentUpdate.documentImagesRequest().stream()
                    .map(image -> {
                        var documentImage = new DocumentImages();
                        documentImage.setUuid(UUID.randomUUID().toString());
                        documentImage.setThumbnail(image);
                        documentImage.setDocument(document);  // Set the updated document as the owner
                        return documentImage;
                    }).toList();

            // Add new images to the document
            document.setImages(images);  // Reassign the images collection

            // Save the new images to the repository
            documentImageRepository.saveAll(images);
        }

        // Handle keywords update
        if (documentUpdate.documentKeywordRequest() != null) {
            // Remove existing keywords (if necessary)
            document.getKeywords().clear();  // Clear current keywords
            List<Keyword> keywords = documentUpdate.documentKeywordRequest().stream()
                    .map(keyword -> {
                        var documentKeyword = new Keyword();
                        documentKeyword.setUuid(UUID.randomUUID().toString());
                        documentKeyword.setKeyword(keyword);
                        documentKeyword.setDocuments(new ArrayList<>(List.of(document))); // Link the keyword to the document
                        return documentKeyword;
                    }).toList();

            // Save the new keywords
            documentKeywordRepository.saveAll(keywords);
            document.setKeywords(keywords);  // Update the document's keywords field
        }

        // Save the updated document
        documentRepository.save(document);

        // Return the updated document as a response
        return documentMapper.mapToDocumentResponse(document);
    }


    @Override
    public void deleteDocument(String uuid) {

        Document document = documentRepository.findByUuid(uuid)

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found with uuid: " + uuid));

        documentRepository.delete(document);

    }

    @Override
    public Page<DocumentResponse> getAllDocumentByPage(int page, int size) {

        if( page < 0 || size < 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Document> documents = documentRepository.findAll(pageRequest);

        return documents.map(documentMapper::mapToDocumentResponse);
    }


}
