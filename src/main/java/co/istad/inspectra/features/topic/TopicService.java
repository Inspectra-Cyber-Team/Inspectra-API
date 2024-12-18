package co.istad.inspectra.features.topic;

import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import co.istad.inspectra.features.topic.dto.TopicResponseDetails;
import org.springframework.data.domain.Page;


/**
 * Service for managing topics
 * @see TopicServiceImpl
 */

public interface TopicService {

    TopicResponse createTopic(TopicRequest topicRequest);

    Page<TopicResponse> getTopics(int page, int size);

    Page<TopicResponseDetails> findByTopicName(String topicName ,int page, int size);

    TopicResponse updateTopic(String uuid, String topicName);

    void deleteTopic(String uuid);



}
