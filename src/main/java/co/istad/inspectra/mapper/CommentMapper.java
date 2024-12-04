package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ReplyMapper.class})
public interface CommentMapper {

    Comment toCommentRequest(CommentRequest commentRequest);

    @Mapping(target = "replies", source = "replies", qualifiedByName = "mapToReplyResponse")
    CommentResponse toCommentResponse(Comment comment);


}
