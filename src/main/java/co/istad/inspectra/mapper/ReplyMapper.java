package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Reply;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReplyMapper {

    Reply mapToReplyRequest(ReplyRequest replyRequest);


    List<ReplyResponse> mapToReplyResponse(List<Reply> replies);

    @Mapping(target = "comment.content", source = "parentComment.content")
    ReplyResponse mapToReplyResponse(Reply reply);




}
