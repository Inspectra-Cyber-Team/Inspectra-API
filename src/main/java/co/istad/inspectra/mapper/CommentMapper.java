package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Comment;
import co.istad.inspectra.features.comment.dto.CommentRequest;
import co.istad.inspectra.features.comment.dto.CommentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toCommentRequest(CommentRequest commentRequest);

    CommentResponse toCommentResponse(Comment comment);


}
