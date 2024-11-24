package co.istad.inspectra.features.userlikeblog;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.LikeBlog;
import co.istad.inspectra.features.blog.BlogRepository;
import co.istad.inspectra.features.userlikeblog.dto.UserLikeBlogResponse;
import co.istad.inspectra.mapper.UserLikeBlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor

public class UserLikeBlogServiceImpl implements UserLikeBlogService {

    private final UserLikeBlogRepository userLikeBlogRepository;

    private final BlogRepository blogRepository;

    private final UserLikeBlogMapper userLikeBlogMapper;


    @Override
    public List<UserLikeBlogResponse> getLikedUsers(String blogUuid) {

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        List<LikeBlog> likeBlogs = userLikeBlogRepository.findByBlogUuid(blogUuid);

        if(likeBlogs.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user liked this blog");
        }

        return likeBlogs.stream()
                .map(userLikeBlogMapper::toUserLikeBlogResponse)
                .toList();

    }
}
