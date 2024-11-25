package co.istad.inspectra.features.blog;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.BlogImages;
import co.istad.inspectra.domain.LikeBlog;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.blog.dto.BlogUpdateRequest;
import co.istad.inspectra.features.blog.dto.UserDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.features.userlikeblog.UserLikeBlogRepository;

import co.istad.inspectra.mapper.BlogMapper;
import co.istad.inspectra.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.istad.inspectra.handler.WebSocketHandler.sessions;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private final UserLikeBlogRepository userLikeBlogRepository;



    @Override
    public BlogResponseDto createBlog(BlogRequestDto blogRequestDto) {

        User user = userRepository.findUserByUuid(blogRequestDto.userUuid());

        if (user == null )
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Blog blog = blogMapper.toVlogRequestDto(blogRequestDto);

        blog.setUuid(UUID.randomUUID().toString());
        blog.setUser(user);
        blog.setDescription(blogRequestDto.description());
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

// Send a WebSocket notification to all connected clients
        notifyClientsAboutNewBlog(blog);

        return blogMapper.toBlogResponseDto(blog);
    }

    private void notifyClientsAboutNewBlog(Blog blog) {
        // Convert the Blog entity to a BlogResponseDto

        blogMapper.toBlogResponseDto(blog);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String message = objectMapper.writeValueAsString(blogMapper.toBlogResponseDto(blog));

            // Send the serialized JSON message to all connected WebSocket clients
            TextMessage webSocketMessage = new TextMessage(message);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(webSocketMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String likeBlog(String blogUuid, @AuthenticationPrincipal CustomUserDetails customUserDetails) {


        if(customUserDetails == null)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        if(blogUuid == null || blogUuid.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid blog uuid is provided");
        }

        String uuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(uuid);

        if(user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }


        Optional<LikeBlog> likeBlogOptional = userLikeBlogRepository.findByBlogUuidAndUserUuid(blogUuid, uuid);

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));


        if(likeBlogOptional.isPresent()) {

            userLikeBlogRepository.delete(likeBlogOptional.get());

            blog.setLikesCount(blog.getLikesCount() - 1);
            blogRepository.save(blog);

            return "Blog unliked successfully";
        }else {

            LikeBlog likeBlog = new LikeBlog();
            likeBlog.setUuid(UUID.randomUUID().toString());
            likeBlog.setBlog(blog);
            likeBlog.setUser(user);

            userLikeBlogRepository.save(likeBlog);

            blog.setLikesCount(blog.getLikesCount() + 1);
            blogRepository.save(blog);

            return "Blog liked successfully";


        }
    }

    @Override
    public void unlikeBlog(String blogUuid) {

            Blog blog = blogRepository.findByUuid(blogUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

            blog.setLikesCount(blog.getLikesCount() - 1);

            blogRepository.save(blog);
    }

    @Override
    public Page<BlogResponseDto> getAllBlogs(int page, int size) {

        if (page < 0 || size < 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size is provided");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);


        return blogRepository.findAll(pageRequest)

                .map(blogMapper::toBlogResponseDto);
    }

    @Override
    public BlogResponseDto getBlog(String blogUuid) {

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        blog.setViewsCount(blog.getViewsCount() + 1);

        blogRepository.save(blog);

        return blogMapper.toBlogResponseDto(blog);

    }

    @Override
    public BlogResponseDto updateBlog(String blogUuid, BlogUpdateRequest blogUpdateRequest) {

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        // Fetch existing images from the blog
        List<BlogImages> existingImages = blog.getBlogImages();

        // Get the updated list of thumbnails from the request
        List<String> updatedThumbnails = blogUpdateRequest.thumbnail();

        // Find and remove obsolete images
        List<BlogImages> imagesToRemove = existingImages.stream()
                .filter(existingImage -> !updatedThumbnails.contains(existingImage.getThumbnail()))
                .toList();
        existingImages.removeAll(imagesToRemove);

        // Add new images that are not already present
        List<String> existingThumbnails = existingImages.stream()
                .map(BlogImages::getThumbnail)
                .toList();

        List<BlogImages> newImages = updatedThumbnails.stream()
                .filter(thumbnail -> !existingThumbnails.contains(thumbnail))
                .map(thumbnail -> {
                    BlogImages newImage = new BlogImages();
                    newImage.setUuid(UUID.randomUUID().toString());
                    newImage.setThumbnail(thumbnail);
                    newImage.setBlog(blog);
                    return newImage;
                })
                .toList();

        existingImages.addAll(newImages);

        // Update the blog's image list
        blog.setBlogImages(existingImages);

        // Update other fields in the blog using the mapper
        blogMapper.UpdateBlog(blog, blogUpdateRequest);

        // Save the updated blog
        blogRepository.save(blog);

        return blogMapper.toBlogResponseDto(blog);
    }

    @Override
    public List<BlogResponseDto> getBlogByUserUuid(String userUuid) {

        User user = userRepository.findUserByUuid(userUuid);

        if (user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<Blog> blogs = blogRepository.findByUserUuid(userUuid);

        if (blogs != null)
        {
            return blogs.stream()
                    .map(blogMapper::toBlogResponseDto)
                    .toList();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No blogs found for the user");
        }

    }


    @Override
    public void deleteBlog(String blogUuid) {

        Blog blog = blogRepository.findByUuid(blogUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found"));

        blogRepository.delete(blog);


    }


}
