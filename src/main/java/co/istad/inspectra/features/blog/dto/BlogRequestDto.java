package co.istad.inspectra.features.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogRequestDto(

        @NotBlank(message = "User uuid is required")
        @Size(max = 100, message = "User uuid must be less than 100 characters")
        String userUuid,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be less than 255 characters")
        String title,

        String description,
        List<String> thumbnail

) {
}

