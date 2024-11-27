package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.LikeBlog;
import co.istad.inspectra.features.userlikeblog.dto.UserLikeBlogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLikeBlogMapper {

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "profile", source = "user.profile")
    @Mapping(target = "bio", source = "user.bio")
    UserLikeBlogResponse toUserLikeBlogResponse(LikeBlog likeBlog);


}
