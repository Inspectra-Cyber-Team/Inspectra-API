package co.istad.inspectra.features.blog;

import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.blog.dto.BlogUpdateRequest;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

/**
 * Service for managing blogs.
 * @see BlogServiceImpl
 * @auth lyhou
 * @version 1.0
 */

public interface BlogService {

  /**
   * Create a new blog.
   * @param blogRequestDto the blog request dto
   * @see BlogRequestDto
   * @return the blog response dto
   */
  BlogResponseDto createBlog(BlogRequestDto blogRequestDto);


    /**
     * Like a blog.
     * @param blogUuid the blog uuid
     * @return the string
     */
  String likeBlog(String blogUuid, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    /**
     * Unlike a blog.
     * @param blogUuid the blog uuid
     */

  void unlikeBlog(String blogUuid);

    /**
     * Get all blogs.
     * @param page the page
     * @param size the size
     * @return the page
     */


  Page<BlogResponseDto> getAllBlogsVerified(int page, int size);


  Page<BlogResponseDto> getAllBlogs(int page, int size);

    /**
     * Get blog.
     * @param blogUuid the blog uuid
     * @return the blog response dto
     */
  BlogResponseDto getBlog(String blogUuid);

    /**
     * Update blog.
     * @param blogUuid the blog uuid
     * @param blogUpdateRequest the blog update request
     * @return the blog response dto
     */

  BlogResponseDto updateBlog(String blogUuid, BlogUpdateRequest blogUpdateRequest);



  /**
   * get blog by user uuid
   * @param userUuid the user uuid
    * @return the list
   * @see BlogResponseDto
   */

  List<BlogResponseDto> getBlogByUserUuid(String userUuid);


  /**
   * Delete blog.
   * @param blogUuid the blog uuid
   */

  void deleteBlog(String blogUuid);


  void verifyBlog(String blogUuid);

  void unverifyBlog(String blogUuid);


}
