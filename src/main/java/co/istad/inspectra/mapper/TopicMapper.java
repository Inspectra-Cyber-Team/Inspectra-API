package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Topic;
import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import co.istad.inspectra.features.topic.dto.TopicResponseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BlogMapper.class})
public interface TopicMapper {

    Topic mapToTopic(TopicRequest topicRequest);

    TopicResponse mapToTopicResponse(Topic topic);

   // @Mapping(target = "blogs", source = "blogs", qualifiedByName = "toBlogResponseDto")
    TopicResponseDetails mapToTopicResponseDetails(Topic topic);


}
