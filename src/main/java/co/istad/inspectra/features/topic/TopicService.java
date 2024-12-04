package co.istad.inspectra.features.topic;

import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import org.springframework.data.domain.Page;


/**
 * Service for managing topics
 * @see TopicServiceImpl
 */

public interface TopicService {

    TopicResponse createTopic(TopicRequest topicRequest);

    Page<TopicResponse> getTopics(int page, int size);

}
