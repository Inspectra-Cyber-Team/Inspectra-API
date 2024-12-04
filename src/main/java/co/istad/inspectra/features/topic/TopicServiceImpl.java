package co.istad.inspectra.features.topic;

import co.istad.inspectra.domain.Topic;
import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import co.istad.inspectra.features.topic.dto.TopicResponseDetails;
import co.istad.inspectra.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class TopicServiceImpl  implements  TopicService {

    private final TopicRepository topicRepository;

    private final TopicMapper topicMapper;


    @Override
    public TopicResponse createTopic(TopicRequest topicRequest) {

        if (topicRepository.existsByName(topicRequest.name())) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "Topic already exists");
        }

        Topic topic = topicMapper.mapToTopic(topicRequest);
        topic.setUuid(UUID.randomUUID().toString());

        return topicMapper.mapToTopicResponse(topicRepository.save(topic));
    }

    @Override
    public Page<TopicResponse> getTopics(int page, int size) {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return topicRepository.findAll(pageRequest).map(topicMapper::mapToTopicResponse);

    }

    @Override
    public Page<TopicResponseDetails> findByTopicName(String topicName,int page, int size) {

        if (page < 0 || size < 0) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid page or size");

        }

        if (topicRepository.findByName(topicName).isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found");

        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);


        return topicRepository.findByName(topicName, pageRequest).map(topicMapper::mapToTopicResponseDetails);

    }
}
