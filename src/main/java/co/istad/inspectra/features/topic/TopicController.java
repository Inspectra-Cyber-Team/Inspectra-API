package co.istad.inspectra.features.topic;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.topic.dto.TopicRequest;
import co.istad.inspectra.features.topic.dto.TopicResponse;
import co.istad.inspectra.features.topic.dto.TopicResponseDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/topics")

public class TopicController {


    private final TopicService topicService;

    @Operation(summary = "Create a new topic")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BaseRestResponse<TopicResponse> createTopic(@Valid @RequestBody TopicRequest topicRequest)
    {

        return BaseRestResponse.<TopicResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .data(topicService.createTopic(topicRequest))
                .message("Topic created successfully")
                .build();

    }


    @Operation(summary = "Get all topics")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<TopicResponse> getTopics(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size)
    {
        return topicService.getTopics(page, size);

    }


    @Operation(summary = "Get all topics details")
    @GetMapping("/{topicName}")
    @ResponseStatus(HttpStatus.OK)
    public Page<TopicResponseDetails> getTopicsDetails(@PathVariable String topicName, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size)
    {
        return topicService.findByTopicName(topicName,page, size);

    }

    @Operation(summary = "Update a topic")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<TopicResponse> updateTopic(@PathVariable String uuid, @RequestParam String topicName)
    {
        return BaseRestResponse.<TopicResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(topicService.updateTopic(uuid, topicName))
                .message("Topic updated successfully")
                .build();
    }

    @Operation(summary = "Delete a topic")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTopic(@PathVariable String uuid)
    {
        topicService.deleteTopic(uuid);
    }

}
