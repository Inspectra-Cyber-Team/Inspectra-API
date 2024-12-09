package co.istad.inspectra.features.userlikecomment;


import co.istad.inspectra.features.userlikecomment.dto.UserLikeCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-like-comments")
@RequiredArgsConstructor

public class UserLikeCommentController {

    private final UserLikeCommentService commentService;

    @GetMapping("/{commentUUid}")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserLikeCommentResponse> likeComment(@PathVariable String commentUUid, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "25") int size) {

        return commentService.likeComment(commentUUid, page, size);

    }



}
