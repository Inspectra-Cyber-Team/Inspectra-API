package co.istad.inspectra.features.documet;


import co.istad.inspectra.domain.Document;
import co.istad.inspectra.domain.DocumentImages;
import co.istad.inspectra.features.documentcategory.DocumentCategoryRepository;
import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
import co.istad.inspectra.mapper.DocumentMapper;
import co.istad.inspectra.repo.DocumentImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentCategoryRepository documentCategoryRepository;

    private final DocumentImageRepository documentImageRepository;

    @Override
    public DocumentResponse createDocument(DocumentRequest documentRequest) {

        var documentCategory = documentCategoryRepository.findByName(documentRequest.documentCategoryName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document category not found with name: "
                        + documentRequest.documentCategoryName()));

        var document = documentMapper.mapToDocument(documentRequest);

        document.setUuid(UUID.randomUUID().toString());
        document.setCategory(documentCategory);
        document.setTitle(documentRequest.title());

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

//        List<Keyword> keywords = documentRequest.documentKeywordRequest().stream()
//                .map(keyword -> {
//                    var documentKeyword = new Keyword();
//                    documentKeyword.setUuid(UUID.randomUUID().toString());
//                    documentKeyword.setDocuments(new ArrayList<>());
//                    documentKeyword.setKeyword(keyword);
//
//                    documentKeywordRepository.save(documentKeyword);
//
//                    return documentKeyword;
//                }).toList();
//
//        document.setKeywords(keywords);


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

        // Fetch existing images from the document
        List<DocumentImages> existingImages = document.getImages();

        if (existingImages == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No images found in the document with uuid: " + uuid);
        }

        // Get the updated list of thumbnails from the request, defaulting to an empty list if null
        List<String> updatedThumbnails = documentUpdate.documentImages();

        if (updatedThumbnails != null) {

            // Find and remove obsolete images
            List<DocumentImages> imagesToRemove = existingImages.stream()
                    .filter(existingImage -> !updatedThumbnails.contains(existingImage.getThumbnail()))
                    .toList();

            existingImages.removeAll(imagesToRemove);

            // Add new images that are not already present
            List<String> existingThumbnails = existingImages.stream()
                    .map(DocumentImages::getThumbnail)
                    .toList();

            List<DocumentImages> newImages = updatedThumbnails.stream()
                    .filter(thumbnail -> !existingThumbnails.contains(thumbnail))
                    .map(thumbnail -> {
                        DocumentImages newImage = new DocumentImages();
                        newImage.setUuid(UUID.randomUUID().toString());
                        newImage.setThumbnail(thumbnail);
                        newImage.setDocument(document);
                        return newImage;
                    })
                    .toList();

            existingImages.addAll(newImages);

            document.setImages(existingImages);

        }

        // Update other document properties using a mapper
        documentMapper.updateDocumentFromRequest(document, documentUpdate);

        // Save updated document
        documentRepository.save(document);

        // Return updated document as a response
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

    @Override
    public List<DocumentResponse> getDocumentByCategory(String categoryUuid) {

        List<Document> documents = documentRepository.findByCategoryUuid(categoryUuid);

        if (documents.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No documents found for category: " + categoryUuid);
        }

        return documents.stream()
                .map(documentMapper::mapToDocumentResponse)
                .toList();

    }


}
