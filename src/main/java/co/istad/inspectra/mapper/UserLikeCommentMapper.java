package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.LikeComment;
import co.istad.inspectra.features.userlikecomment.dto.UserLikeCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLikeCommentMapper {

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "profile", source = "user.profile")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "bio", source = "user.bio")
    UserLikeCommentResponse toUserLikeCommentResponse(LikeComment likeComment);



}
