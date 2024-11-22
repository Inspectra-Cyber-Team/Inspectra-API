package co.istad.inspectra.features.blog;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.BlogImages;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;


    @Override
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto) {

        User user = userRepository.findUserByUuid(blogRequestDto.userUuid());

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Blog blog = blogMapper.toVlogRequestDto(blogRequestDto);

        blog.setUuid(UUID.randomUUID().toString());
        blog.setUser(user);
        blog.setDescription(blogRequestDto.content());
        blog.setTitle(blogRequestDto.title());

        List<BlogImages> blogImages = blogRequestDto.thumbnail()
                .stream()
                .map(url -> {

                    BlogImages blogImage = new BlogImages();
                    blogImage.setUuid(UUID.randomUUID().toString());
                    blogImage.setThumbnail(url);
                    blogImage.setBlog(blog);

                    return blogImage;

                }).toList();

        blog.setBlogImages(blogImages);

        blogRepository.save(blog);

        return blogMapper.toBlogResponseDto(blog);
    }

    @Override
    public String likeBlog(String blogUuid) {

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        blog.setLikesCount(blog.getLikesCount() + 1);

        blogRepository.save(blog);

        return " Blog liked successfully";
    }

    @Override
    public void unlikeBlog(String blogUuid) {

            Blog blog = blogRepository.findByUuid(blogUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

            blog.setLikesCount(blog.getLikesCount() - 1);

            blogRepository.save(blog);
    }

    @Override
    public List<BlogResponseDto> getAllBlogs() {

        return blogRepository.findAll()
                .stream()
                .map(blogMapper::toBlogResponseDto)
                .toList();
    }


}
