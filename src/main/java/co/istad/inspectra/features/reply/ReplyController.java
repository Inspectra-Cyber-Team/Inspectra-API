package co.istad.inspectra.features.reply;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/replies")
@RequiredArgsConstructor

public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Create a reply")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<ReplyResponse> createReply(@Valid @RequestBody ReplyRequest request) {

        return BaseRestResponse.<ReplyResponse>builder()
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(replyService.createReply(request))
                .message("Reply created successfully")
                .build();

    }


    @Operation(summary = "Get all replies")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<ReplyResponse>> getAllReplies() {
        return BaseRestResponse.<List<ReplyResponse>>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(replyService.getAllReplies())
                .message("Replies retrieved successfully")
                .build();
    }

    @Operation(summary = "Get a reply by uuid")
    @GetMapping("/{replyUuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<ReplyResponse> getReplyByUuid(@PathVariable String replyUuid) {
        return BaseRestResponse.<ReplyResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(replyService.getReplyByUuid(replyUuid))
                .message("Reply retrieved successfully")
                .build();
    }


    @Operation(summary = "Get a reply by comment id")
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<ReplyResponse>> getReplyByCommentId(@PathVariable String commentId) {
        return BaseRestResponse.<List<ReplyResponse>>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(replyService.getReplyByCommentId(commentId))
                .message("Replies retrieved successfully")
                .build();
    }

    @Operation(summary = "Like a reply")
    @PostMapping("/{replyUuid}/like")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<String> likeReply(@PathVariable String replyUuid) {
        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(replyService.likeReply(replyUuid))
                .message("Reply liked successfully")
                .build();
    }

    @Operation(summary = "Unlike a reply")
    @DeleteMapping("/{replyUuid}/unlike")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<String> unlikeReply(@PathVariable String replyUuid) {
        replyService.unlikeReply(replyUuid);
        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Reply unliked successfully")
                .build();
    }


    @Operation(summary = "Delete a reply")
    @DeleteMapping("/{replyUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<String> deleteReply(@PathVariable String replyUuid) {
        replyService.deleteReply(replyUuid);
        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NO_CONTENT.value())
                .message("Reply deleted successfully")
                .build();
    }

    @Operation(summary = "Update a reply")
    @PutMapping("/{replyUuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<ReplyResponse> updateReply(@PathVariable String replyUuid, @RequestBody String content) {

        return BaseRestResponse.<ReplyResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Reply updated successfully")
                .data( replyService.updateReply(replyUuid, content))
                .build();
    }

}
