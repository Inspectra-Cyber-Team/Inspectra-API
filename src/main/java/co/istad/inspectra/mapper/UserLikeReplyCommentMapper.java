package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.LikeReply;

import co.istad.inspectra.features.userlikereplycomment.dto.UserLikeReplyCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserLikeReplyCommentMapper {

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "profile", source = "user.profile")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "bio", source = "user.bio")
    UserLikeReplyCommentResponse toUserLikeCommentResponse(LikeReply likeReply);


}
