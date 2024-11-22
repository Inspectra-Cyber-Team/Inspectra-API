package co.istad.inspectra.features.blog;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor

public class BlogController {

    private final BlogService blogService;


    @Operation(summary = "Create a blog")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<BlogResponseDto> createBlog(@Valid @RequestBody BlogRequestDto blogRequestDto)
    {
        return BaseRestResponse.<BlogResponseDto>builder()
                .data(blogService.createBlog(blogRequestDto))
                .message("Blog created successfully")
                .build();

    }

    @Operation(summary = "Get all blogs")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<BlogResponseDto>> getAllBlogs()
    {
        return BaseRestResponse.<List<BlogResponseDto>>builder()
                .data(blogService.getAllBlogs())
                .message("Blogs fetched successfully")
                .build();
    }

    @Operation(summary = "Unlike a blog")
    @DeleteMapping("/{blogUuid}/unlike")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> unlikeBlog(@PathVariable String blogUuid)
    {
        blogService.unlikeBlog(blogUuid);
        return BaseRestResponse.<String>builder()
                .message("Blog unliked successfully")
                .build();
    }


    @Operation(summary = "Like a blog")
    @PostMapping("/{blogUuid}/like")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> likeBlog(@PathVariable String blogUuid)
    {
        return BaseRestResponse.<String>builder()
                .data(blogService.likeBlog(blogUuid))
                .message("Blog liked successfully")
                .build();
    }





}
