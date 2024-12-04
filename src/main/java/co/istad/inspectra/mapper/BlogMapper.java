package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.BlogImages;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.blog.dto.BlogUpdateRequest;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "topic", ignore = true)
    Blog toVlogRequestDto(BlogRequestDto blogRequestDto);


    @Mapping(target = "thumbnail", source = "blogImages")
//    @Mapping(target = "countComments", expression = "java(blog.getComments() != null ? blog.getComments().size() : 0)")
//    @Mapping(target = "countComments", expression = "java((blog.getComments() != null ? blog.getComments().size() : 0) + (blog.getReplies() != null ? blog.getReplies().size() : 0))")

    @Named("toBlogResponseDto")
    @Mapping(target = "countComments", expression = "java(blog.getTotalInteractions())")
    BlogResponseDto toBlogResponseDto(Blog blog);


    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void UpdateBlog(@MappingTarget Blog blog, BlogUpdateRequest blogUpdateRequest);


    default List<String> mapImagesToUrls(List<BlogImages> images) {
        return images != null ? images.stream()
                .map(BlogImages::getThumbnail)
                .collect(Collectors.toList()) : null;
    }



}
