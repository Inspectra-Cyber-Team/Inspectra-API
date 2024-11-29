package co.istad.inspectra.features.documentcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record DocumentCategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be less than 255 characters")
        String name,

        String description,
        List<String> documentImagesRequest

) {
}
