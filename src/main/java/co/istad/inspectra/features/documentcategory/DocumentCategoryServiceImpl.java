package co.istad.inspectra.features.documentcategory;

import co.istad.inspectra.domain.DocumentCategory;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryDetails;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryRequest;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryUpdate;
import co.istad.inspectra.mapper.DocumentCategoryMapper;
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

    @Override
    public DocumentCategoryResponse create(DocumentCategoryRequest documentCategoryRequest) {

        DocumentCategory category = documentCategoryMapper.mapToDocumentCategory(documentCategoryRequest);

        category.setUuid(UUID.randomUUID().toString());

        return documentCategoryMapper.mapToDocumentCategoryResponse(documentCategoryRepository.save(category));
    }

    @Override
    public DocumentCategoryResponse update(String uuid, DocumentCategoryUpdate documentCategoryUpdate) {

        DocumentCategory category = documentCategoryRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Document Category not found with UUID: "+uuid)
        );

        documentCategoryMapper.updateDocumentCategory(category,documentCategoryUpdate);

        return documentCategoryMapper.mapToDocumentCategoryResponse(documentCategoryRepository.save(category));

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
