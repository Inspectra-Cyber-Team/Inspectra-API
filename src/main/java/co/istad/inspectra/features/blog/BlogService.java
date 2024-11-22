package co.istad.inspectra.features.blog;

import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;

import java.util.List;

public interface BlogService {

  BlogResponseDto createBlog(BlogRequestDto blogRequestDto);

  String likeBlog(String blogUuid);

  void unlikeBlog(String blogUuid);

  List<BlogResponseDto> getAllBlogs();

}
