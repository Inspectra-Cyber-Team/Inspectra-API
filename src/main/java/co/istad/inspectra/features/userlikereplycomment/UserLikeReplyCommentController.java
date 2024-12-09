package co.istad.inspectra.features.userlikereplycomment;

import co.istad.inspectra.features.userlikereplycomment.dto.UserLikeReplyCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-like-reply-comment")
@RequiredArgsConstructor

public class UserLikeReplyCommentController {

    private final UserLikeReplyCommentService userLikeReplyCommentService;

    @GetMapping("/{replyUuid}")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserLikeReplyCommentResponse> getUserLikeReplyCommentByReplyUuid(@PathVariable String replyUuid,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        return userLikeReplyCommentService.likeReplyComment(replyUuid, page, size);
    }

}
