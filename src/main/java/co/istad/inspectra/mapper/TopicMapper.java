package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Topic;
import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic mapToTopic(TopicRequest topicRequest);

    TopicResponse mapToTopicResponse(Topic topic);


}
