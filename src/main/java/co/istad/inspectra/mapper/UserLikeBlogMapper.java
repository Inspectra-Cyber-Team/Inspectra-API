package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.LikeBlog;
import co.istad.inspectra.features.userlikeblog.dto.UserLikeBlogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLikeBlogMapper {

    @Mapping(target = "uuid", source = "blog.user.uuid")
    @Mapping(target = "firstName", source = "blog.user.firstName")
    @Mapping(target = "lastName", source = "blog.user.lastName")
    @Mapping(target = "profile", source = "blog.user.profile")
    @Mapping(target = "bio", source = "blog.user.bio")
    UserLikeBlogResponse toUserLikeBlogResponse(LikeBlog likeBlog);


}
