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
        String title,

        String content,
        List<String> thumbnail

) {
}

