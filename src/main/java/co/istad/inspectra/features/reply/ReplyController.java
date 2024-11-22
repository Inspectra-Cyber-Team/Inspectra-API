package co.istad.inspectra.features.reply;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/replies")
@RequiredArgsConstructor

public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "Create a reply")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public BaseRestResponse<ReplyResponse> createReply(@Valid @RequestBody ReplyRequest request) {

        return BaseRestResponse.<ReplyResponse>builder()
                .data(replyService.createReply(request))
                .message("Reply created successfully")
                .build();

    }


    @Operation(summary = "Get all replies")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<ReplyResponse>> getAllReplies() {
        return BaseRestResponse.<List<ReplyResponse>>builder()
                .data(replyService.getAllReplies())
                .message("Replies retrieved successfully")
                .build();
    }


    @Operation(summary = "Get a reply by comment id")
    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<ReplyResponse>> getReplyByCommentId(@PathVariable String commentId) {
        return BaseRestResponse.<List<ReplyResponse>>builder()
                .data(replyService.getReplyByCommentId(commentId))
                .message("Replies retrieved successfully")
                .build();
    }

    @Operation(summary = "Like a reply")
    @PostMapping("/{replyUuid}/like")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> likeReply(@PathVariable String replyUuid) {
        return BaseRestResponse.<String>builder()
                .data(replyService.likeReply(replyUuid))
                .message("Reply liked successfully")
                .build();
    }

    @Operation(summary = "Unlike a reply")
    @DeleteMapping("/{replyUuid}/unlike")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> unlikeReply(@PathVariable String replyUuid) {
        replyService.unlikeReply(replyUuid);
        return BaseRestResponse.<String>builder()
                .message("Reply unliked successfully")
                .build();
    }

}
