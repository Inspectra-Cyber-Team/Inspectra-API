package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.BlogImages;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    Blog toVlogRequestDto(BlogRequestDto blogRequestDto);


    @Mapping(target = "thumbnail", source = "blogImages")
    BlogResponseDto toBlogResponseDto(Blog blog);


    default List<String> mapImagesToUrls(List<BlogImages> images) {
        return images != null ? images.stream()
                .map(BlogImages::getThumbnail)
                .collect(Collectors.toList()) : null;
    }



}
