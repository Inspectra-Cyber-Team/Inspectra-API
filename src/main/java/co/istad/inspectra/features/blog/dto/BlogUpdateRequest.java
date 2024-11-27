package co.istad.inspectra.features.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogUpdateRequest(
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,
        String description,
        List<String> thumbnail
) {
}
