package co.istad.inspectra.features.userlikeblog;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.userlikeblog.dto.UserLikeBlogResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user-like-blog")
@RequiredArgsConstructor

public class UserLikeBlogController {

    private final UserLikeBlogService userLikeBlogService;


    @Operation(summary = "Get all users who liked a blog")
    @GetMapping("/{blogUuid}")
    @ResponseStatus(HttpStatus.OK)

    public BaseRestResponse<List<UserLikeBlogResponse>> getAllUsersWhoLikedBlog(@PathVariable String blogUuid){

        return BaseRestResponse.<List<UserLikeBlogResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(userLikeBlogService.getLikedUsers(blogUuid))
                .timestamp(LocalDateTime.now())
                .message("Users who liked the blog")
                .build();
    }



}
