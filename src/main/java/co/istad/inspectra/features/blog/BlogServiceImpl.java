package co.istad.inspectra.features.blog;

import co.istad.inspectra.domain.Blog;
import co.istad.inspectra.domain.BlogImages;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.blog.dto.BlogRequestDto;
import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.blog.dto.BlogUpdateRequest;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;


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
