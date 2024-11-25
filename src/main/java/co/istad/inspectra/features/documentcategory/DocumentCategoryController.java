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

import java.util.List;

@RestController
@RequestMapping("/api/v1/document-category")
@RequiredArgsConstructor

public class DocumentCategoryController {

    private final DocumentCategoryService documentCategoryService;

    @Operation(summary = "Get all document categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<DocumentCategoryResponse>> getAllDocumentCategories() {

        return BaseRestResponse.<List<DocumentCategoryResponse>>
                builder()
                .status(HttpStatus.OK.value())
                .data(documentCategoryService.getAll())
                .build();

    }

    @Operation(summary = "Get all document categories by page")
    @GetMapping("/page")
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
                .build();

    }

    @Operation(summary = "Get document category by name")
    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<DocumentCategoryResponse> getDocumentCategoryByName(@PathVariable String name) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.OK.value())
                .data(documentCategoryService.getByName(name))
                .build();

    }


    @Operation(summary = "Create a new document category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public BaseRestResponse<DocumentCategoryResponse> createDocumentCategory(@Valid @RequestBody DocumentCategoryRequest documentCategoryRequest) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.CREATED.value())
                .data(documentCategoryService.create(documentCategoryRequest))
                .build();

    }


    @Operation(summary = "Update an existing document category")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)

    public BaseRestResponse<DocumentCategoryResponse> updateDocumentCategory(@PathVariable String uuid, @Valid @RequestBody DocumentCategoryUpdate documentCategoryUpdate) {

        return BaseRestResponse.<DocumentCategoryResponse>
                builder()
                .status(HttpStatus.OK.value())
                .data(documentCategoryService.update(uuid, documentCategoryUpdate))
                .build();

    }


    @Operation(summary = "Delete a document category")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    public BaseRestResponse<Object> deleteDocumentCategory(@PathVariable String uuid) {

        documentCategoryService.delete(uuid);

        return BaseRestResponse
                .builder()
                .status(HttpStatus.NO_CONTENT.value())
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
                .data(documentCategoryService.getAllDocumentCategories())
                .build();

    }




}
