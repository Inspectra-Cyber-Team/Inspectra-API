package co.istad.inspectra.features.documentcategory;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryDetails;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryRequest;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryResponse;
import co.istad.inspectra.features.documentcategory.dto.DocumentCategoryUpdate;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/document-categories")
@RequiredArgsConstructor

public class DocumentCategoryController {

    private final DocumentCategoryService documentCategoryService;

    @Operation(summary = "Get all document categories")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<DocumentCategoryResponse>> getAllDocumentCategories() {

        return BaseRestResponse.<List<DocumentCategoryResponse>>
                builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(documentCategoryService.getAll())
                .message("Document categories have been retrieved successfully.")
                .build();

    }

    @Operation(summary = "Get all document categories by page")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<DocumentCategoryResponse> getAllDocumentCategoriesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {

        return documentCategoryService.getAll(page, size);

    }

    @Operation(summary = "Get document category by UUID")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<DocumentCategoryResponse> getDocumentCategoryByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.OK.value())
                .data(documentCategoryService.getByUuid(uuid))
                .timestamp(LocalDateTime.now())
                .message("Document category has been retrieved successfully.")
                .build();

    }

    @Operation(summary = "Get document category by name")
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<DocumentCategoryResponse> getDocumentCategoryByName(@PathVariable String name) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Document category has been retrieved successfully.")
                .data(documentCategoryService.getByName(name))
                .build();

    }


    @Operation(summary = "Create a new document category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<DocumentCategoryResponse> createDocumentCategory(@Valid @RequestBody DocumentCategoryRequest documentCategoryRequest) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .data(documentCategoryService.create(documentCategoryRequest))
                .message("Document category has been created successfully.")
                .build();

    }


    @Operation(summary = "Update an existing document category")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<DocumentCategoryResponse> updateDocumentCategory(@PathVariable String uuid, @Valid @RequestBody DocumentCategoryUpdate documentCategoryUpdate) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentCategoryService.update(uuid, documentCategoryUpdate))
                .message("Document category has been updated successfully.")
                .build();

    }


    @Operation(summary = "Delete a document category")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<Object> deleteDocumentCategory(@PathVariable String uuid) {

        documentCategoryService.delete(uuid);

        return BaseRestResponse
                .builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Document category has been deleted successfully.")
                .build();

    }


    @Operation(summary = "Get all document categories")
    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<DocumentCategoryDetails>> getAllDocumentCategoriesDetails() {

        return BaseRestResponse.<List<DocumentCategoryDetails>>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentCategoryService.getAllDocumentCategories())
                .message("Document categories details have been retrieved successfully.")
                .build();

    }




}
