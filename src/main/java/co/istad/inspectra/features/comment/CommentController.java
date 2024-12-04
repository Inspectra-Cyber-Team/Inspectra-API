package co.istad.inspectra.features.comment;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import co.istad.inspectra.features.comment.dto.UpdateComment;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create a comment")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<CommentResponse> createComment(@Valid @RequestBody CommentRequest commentRequest,  @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return BaseRestResponse.<CommentResponse>
                builder()
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(commentService.createComment(commentRequest, customUserDetails))
                .message("Comment created successfully")
                .build();
    }

    @Operation(summary = "Get all comments")
    @GetMapping
    public Page<CommentResponse> getAllComments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size)
    {
        return commentService.getAllComments(page, size);
    }


    @Operation(summary = "Like a comment")
    @PostMapping("/{commentUuid}/like")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<String> likeComment(@PathVariable String commentUuid)
    {
        return BaseRestResponse.<String>
                builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(commentService.likeComment(commentUuid))
                .message("Comment liked successfully")
                .build();
    }

    @Operation(summary = "Delete like a comment")
    @DeleteMapping("/{commentUuid}/unlike")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<String> deleteLikeComment(@PathVariable String commentUuid)
    {
        commentService.deleteLikeComment(commentUuid);
        return BaseRestResponse.<String>
                builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Comment like deleted successfully")
                .build();
    }

    @Operation(summary = "Update a comment")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<CommentResponse> updateComment(@PathVariable String uuid, @RequestBody UpdateComment updateComment)
    {
        return BaseRestResponse.<CommentResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(commentService.updateComment(uuid, updateComment))
                .message("Comment updated successfully")
                .build();
    }


    @Operation(summary = "Delete a comment")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<String> deleteComment(@PathVariable String uuid)
    {
        commentService.deleteComment(uuid);
        return BaseRestResponse.<String>
                builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Comment deleted successfully")
                .build();
    }


    @GetMapping("/{blogUuid}/blog")
    public Page<CommentResponse> getCommentsByBlogUuid(@PathVariable String blogUuid, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size)
    {
        return commentService.getCommentsByBlogUuid(blogUuid, page, size);
    }
}
