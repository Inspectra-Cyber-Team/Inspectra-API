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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor

public class BlogController {

    private final BlogService blogService;


    @Operation(summary = "Create a blog")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<BlogResponseDto> createBlog(@Valid @RequestBody BlogRequestDto blogRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return BaseRestResponse.<BlogResponseDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .data(blogService.createBlog(blogRequestDto,customUserDetails))
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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<BlogResponseDto> getAllBlogs(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "25") int size)
    {
        return blogService.getAllBlogs(page, size);

    }


    @Operation(summary = "Like a blog")
    @PostMapping("/{blogUuid}/like")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<String> likeBlog(@PathVariable String blogUuid,@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
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
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(blogService.getBlog(blogUuid))
                .message("Blog retrieved successfully")
                .build();
    }


    @Operation(summary = "Update a blog")
    @PutMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<BlogResponseDto> updateBlog(@PathVariable String blogUuid, @Valid @RequestBody BlogUpdateRequest blogUpdateRequest)
    {
        return BaseRestResponse.<BlogResponseDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(blogService.updateBlog(blogUuid, blogUpdateRequest))
                .message("Blog updated successfully")
                .build();
    }


    @Operation(summary = "Get all blogs by user")
    @GetMapping("/user/{userUuid}")
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogResponseDto> getBlogsByUser(@PathVariable String userUuid,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "25") int size)
    {
       return blogService.getBlogByUserUuid(userUuid,page,size);
    }




    @Operation(summary = "Delete a blog")
    @DeleteMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<String> deleteBlog(@PathVariable String blogUuid)
    {
        blogService.deleteBlog(blogUuid);
        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NO_CONTENT.value())
                .message("Blog deleted successfully")
                .build();
    }


    @Operation(summary = "Verify a blog")
    @PutMapping("/{blogUuid}/verify")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<BlogResponseDto> verifyBlog(@PathVariable String blogUuid) throws MessagingException {

        blogService.verifyBlog(blogUuid);

        return BaseRestResponse.<BlogResponseDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Blog verified successfully")
                .build();
    }

    @Operation(summary = "Unverify a blog")
    @PutMapping("/{blogUuid}/unverified")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<BlogResponseDto> unverifiedBlog(@PathVariable String blogUuid)
    {
        blogService.unverifyBlog(blogUuid);

        return BaseRestResponse.<BlogResponseDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Blog unverified successfully")
                .build();
    }

    @Operation(summary = "Count all blogs")
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<Integer> countAllBlogs()
    {
        return BaseRestResponse.<Integer>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(blogService.countAllBlogs())
                .message("Counted all blogs successfully")
                .build();
    }






}
