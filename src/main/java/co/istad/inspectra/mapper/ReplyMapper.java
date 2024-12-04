package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Reply;
import co.istad.inspectra.features.reply.dto.ReplyRequest;
import co.istad.inspectra.features.reply.dto.ReplyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReplyMapper {

    Reply mapToReplyRequest(ReplyRequest replyRequest);

    @Named("mapToReplyResponse")
    List<ReplyResponse> mapToReplyResponse(List<Reply> replies);
    
    ReplyResponse mapToReplyResponse(Reply reply);




}
