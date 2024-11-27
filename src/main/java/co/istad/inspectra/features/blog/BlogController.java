package co.istad.inspectra.features.blog;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.blog.dto.BlogUpdateRequest;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                .status(HttpStatus.CREATED.value())
                .data(blogService.createBlog(blogRequestDto))
                .message("Blog created successfully")
                .build();

    }

    @Operation(summary = "Get all blogs")
    @GetMapping("/verified")
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogResponseDto> getAllBlogsVerified(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "25") int size)
    {
        return blogService.getAllBlogsVerified(page, size);
    }

    @Operation(summary = "Get all blogs")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogResponseDto> getAllBlogs(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "25") int size)
    {
        return blogService.getAllBlogs(page, size);

    }

    @Operation(summary = "Unlike a blog")
    @DeleteMapping("/{blogUuid}/unlike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<String> unlikeBlog(@PathVariable String blogUuid)
    {
        blogService.unlikeBlog(blogUuid);
        return BaseRestResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Blog unliked successfully")
                .build();
    }


    @Operation(summary = "Like a blog")
    @PostMapping("/{blogUuid}/like")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> likeBlog(@PathVariable String blogUuid,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .data(blogService.likeBlog(blogUuid,customUserDetails))
                .message("Blog liked successfully")
                .build();
    }


    @Operation(summary = "Get a blog")
    @GetMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<BlogResponseDto> getBlog(@PathVariable String blogUuid)
    {
        return BaseRestResponse.<BlogResponseDto>builder()
                .status(HttpStatus.OK.value())
                .data(blogService.getBlog(blogUuid))
                .message("Blog retrieved successfully")
                .build();
    }


    @Operation(summary = "Update a blog")
    @PutMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<BlogResponseDto> updateBlog(@PathVariable String blogUuid, @Valid @RequestBody BlogUpdateRequest blogUpdateRequest)
    {
        return BaseRestResponse.<BlogResponseDto>builder()
                .status(HttpStatus.OK.value())
                .data(blogService.updateBlog(blogUuid, blogUpdateRequest))
                .message("Blog updated successfully")
                .build();
    }


    @Operation(summary = "Get all blogs by user")
    @GetMapping("/user/{userUuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<BlogResponseDto>> getBlogsByUser(@PathVariable String userUuid)
    {
        return BaseRestResponse.<List<BlogResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .data(blogService.getBlogByUserUuid(userUuid))
                .message("Blogs retrieved successfully")
                .build();
    }




    @Operation(summary = "Delete a blog")
    @DeleteMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<String> deleteBlog(@PathVariable String blogUuid)
    {
        blogService.deleteBlog(blogUuid);
        return BaseRestResponse.<String>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Blog deleted successfully")
                .build();
    }


    @Operation(summary = "Verify a blog")
    @PutMapping("/{blogUuid}/verify")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<BlogResponseDto> verifyBlog(@PathVariable String blogUuid) throws MessagingException {

        blogService.verifyBlog(blogUuid);

        return BaseRestResponse.<BlogResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Blog verified successfully")
                .build();
    }

    @Operation(summary = "Unverify a blog")
    @PutMapping("/{blogUuid}/unverified")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<BlogResponseDto> unverifiedBlog(@PathVariable String blogUuid)
    {
        blogService.unverifyBlog(blogUuid);

        return BaseRestResponse.<BlogResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Blog unverified successfully")
                .build();
    }




}
