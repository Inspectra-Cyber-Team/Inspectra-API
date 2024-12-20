package co.istad.inspectra.features.documentcategory;

import co.istad.inspectra.domain.DocumentCategory;
import co.istad.inspectra.domain.DocumentImages;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryDetails;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryRequest;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryUpdate;
import co.istad.inspectra.mapper.DocumentCategoryMapper;
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

public class DocumentCategoryServiceImpl implements DocumentCategoryService {

    private final DocumentCategoryMapper documentCategoryMapper;

    private final DocumentCategoryRepository documentCategoryRepository;

    private final DocumentImageRepository documentImageRepository;

    @Override
    public DocumentCategoryResponse create(DocumentCategoryRequest documentCategoryRequest) {

        if(documentCategoryRepository.existsByName(documentCategoryRequest.name())){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Document Category already exists with name: "+documentCategoryRequest.name());

        }

        DocumentCategory category = documentCategoryMapper.mapToDocumentCategory(documentCategoryRequest);

        category.setUuid(UUID.randomUUID().toString());

        documentCategoryRepository.save(category);

        List<String> imagesRequest = documentCategoryRequest.documentImagesRequest();

        if (imagesRequest!=null) {

            List<DocumentImages> images = documentCategoryRequest.documentImagesRequest().stream()
                    .map(image -> {
                        DocumentImages documentImage = new DocumentImages();
                        documentImage.setUuid(UUID.randomUUID().toString());
                        documentImage.setThumbnail(image);
                        documentImage.setCategory(category);

                        documentImageRepository.save(documentImage);

                        return documentImage;
                    }).toList();

            category.setImages(images);
        }

        return documentCategoryMapper.mapToDocumentCategoryResponse(category);
    }

    @Override
    public DocumentCategoryResponse update(String uuid, DocumentCategoryUpdate documentCategoryUpdate) {

        DocumentCategory category = documentCategoryRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Category not found with UUID: "+uuid)
        );

        List<DocumentImages> existingImages = category.getImages();

        if (existingImages == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Images not found with UUID: "+uuid);
        }

        List<String> updatedImages = documentCategoryUpdate.documentCategoryImages();

        if (updatedImages != null)
        {

            // Find and remove obsolete images
            List<DocumentImages> imagesToRemove = existingImages.stream()
                    .filter(existingImage -> !updatedImages.contains(existingImage.getThumbnail()))
                    .toList();

            existingImages.removeAll(imagesToRemove);

            // Add new images that are not already present
            List<String> existingThumbnails = existingImages.stream()
                    .map(DocumentImages::getThumbnail)
                    .toList();

            List<DocumentImages> newImages = updatedImages.stream()
                    .filter(thumbnail -> !existingThumbnails.contains(thumbnail))
                    .map(thumbnail -> {
                        DocumentImages newImage = new DocumentImages();
                        newImage.setUuid(UUID.randomUUID().toString());
                        newImage.setThumbnail(thumbnail);
                        newImage.setCategory(category);
                        return newImage;
                    })
                    .toList();

            existingImages.addAll(newImages);

            category.setImages(existingImages);


        }


        documentCategoryMapper.updateDocumentCategory(category,documentCategoryUpdate);

        documentCategoryRepository.save(category);

        return documentCategoryMapper.mapToDocumentCategoryResponse(category);

    }

    @Override
    public void delete(String uuid) {

        DocumentCategory category = documentCategoryRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Category not found with UUID: "+uuid)
        );

        documentCategoryRepository.delete(category);


    }

    @Override
    public DocumentCategoryResponse getByUuid(String uuid) {

        DocumentCategory category = documentCategoryRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Category not found with UUID: "+uuid)
        );

        return documentCategoryMapper.mapToDocumentCategoryResponse(category);

    }

    @Override
    public DocumentCategoryResponse getByName(String name) {

        DocumentCategory category = documentCategoryRepository.findByName(name).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Category not found with name: "+name)
        );

        return documentCategoryMapper.mapToDocumentCategoryResponse(category);

    }

    @Override
    public List<DocumentCategoryResponse> getAll() {

        List<DocumentCategory> categories = documentCategoryRepository.findAll();

        return categories.stream().map(documentCategoryMapper::mapToDocumentCategoryResponse).toList();
    }


    @Override
    public Page<DocumentCategoryResponse> getAll(int page, int size) {

        if(page<0 || size<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Page and Size must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.ASC,"createdAt");

        PageRequest pageRequest = PageRequest.of(page,size,sort);

        Page<DocumentCategory> categories = documentCategoryRepository.findAll(pageRequest);

        return categories.map(documentCategoryMapper::mapToDocumentCategoryResponse);
    }

    @Override
    public List<DocumentCategoryDetails> getAllDocumentCategories() {

        List<DocumentCategory> categories = documentCategoryRepository.findAll();

        return categories.stream().map(documentCategoryMapper::mapToDocumentCategoryDetails).toList();
    }
}
