package co.istad.inspectra.features.documet;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.documet.dto.DocumentRequest;
import co.istad.inspectra.features.documet.dto.DocumentResponse;
import co.istad.inspectra.features.documet.dto.DocumentUpdate;
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
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor

public class DocumentController {

    private final DocumentService documentService;

    @Operation(summary = "Create document")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<DocumentResponse> createDocument(@Valid @RequestBody DocumentRequest documentRequest) {

        return BaseRestResponse.<DocumentResponse>builder()
                .data(documentService.createDocument(documentRequest))
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .message("Document created successfully")
                .build();


    }

    @Operation(summary = "Get document by uuid")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<DocumentResponse> getDocumentByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<DocumentResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentService.getDocumentByUuid(uuid))
                .message("Document retrieved successfully")
                .build();
    }



    @Operation(summary = "Get all documents")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<DocumentResponse>> getAllDocuments() {

        return BaseRestResponse.<List<DocumentResponse>>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentService.getAllDocument())
                .message("Documents retrieved successfully")
                .build();
    }



    @Operation(summary = "Update document")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<DocumentResponse> updateDocument(@PathVariable String uuid, @Valid @RequestBody DocumentUpdate documentUpdate) {

        return BaseRestResponse.<DocumentResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentService.updateDocument(uuid, documentUpdate))
                .message("Document updated successfully")
                .build();
    }


    @Operation(summary = "Delete document")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<Void> deleteDocument(@PathVariable String uuid) {

        documentService.deleteDocument(uuid);

        return BaseRestResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Document deleted successfully")
                .build();
    }


    @Operation(summary = "Get all documents by page")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<DocumentResponse> getAllDocumentByPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "25") int size) {

        return documentService.getAllDocumentByPage(page, size);

    }


    @Operation(summary = "Get document by category")
    @GetMapping("/category/{categoryUuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<DocumentResponse>> getDocumentsByCategory(@PathVariable String categoryUuid) {

        return BaseRestResponse.<List<DocumentResponse>>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(documentService.getDocumentByCategory(categoryUuid))
                .message("Documents retrieved successfully")
                .build();
    }




}
