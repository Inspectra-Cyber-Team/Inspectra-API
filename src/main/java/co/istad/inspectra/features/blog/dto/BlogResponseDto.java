package co.istad.inspectra.features.blog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BlogResponseDto(
        String uuid,
        String title,
        int likesCount,
       int viewsCount,
       int countComments,

       Boolean isVerified,
       String description,
       List<String> thumbnail,
       UserDto user,
        String createdAt,
        String lastModifiedAt

) {
}
