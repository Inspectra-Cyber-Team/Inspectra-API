package co.istad.inspectra.features.documet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record DocumentRequest(

        @NotBlank(message = "Document category name is required")
        @Size(max = 100, message = "Document category name must be less than 100 characters")
        String documentCategoryName,


        @Size(max = 100, message = "Document name must be less than 100 characters")
        @NotBlank(message = "Document description is required")
        String title,


        String description,
        List<String> documentImagesRequest


) {
}
