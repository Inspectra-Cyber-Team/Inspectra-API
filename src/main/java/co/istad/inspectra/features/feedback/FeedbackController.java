package co.istad.inspectra.features.feedback;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.feedback.dto.FeedBackUpdate;
import co.istad.inspectra.features.feedback.dto.FeedbackRequest;
import co.istad.inspectra.features.feedback.dto.FeedbackResponse;
import co.istad.inspectra.features.feedback.dto.FeedbackResponseDetails;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@RequiredArgsConstructor

public class FeedbackController {

    private final FeedBackService feedBackService;

    @Operation(summary = "Get all feedback")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<FeedbackResponse> getFeedbacks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "25") int limit) {

        return feedBackService.getFeedBacks(page, limit);

    }


    @Operation(summary = "Get feedback by uuid")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FeedbackResponse> getFeedbackByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .message("Get feedback by uuid")
                .data(feedBackService.getFeedBackByUuid(uuid))
                .build();

    }


    @Operation(summary = "Create feedback")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<FeedbackResponse> createFeedback(@AuthenticationPrincipal CustomUserDetails customUserDetails,@Valid @RequestBody FeedbackRequest request) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .message("Create feedback")
                .data(feedBackService.createFeedBack(customUserDetails,request))
                .build();

    }

    @Operation(summary = "Update feedback")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FeedbackResponse> updateFeedback(@Valid @RequestBody  FeedBackUpdate feedBackUpdate, @PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .message("Update feedback")
                .data(feedBackService.updateFeedBack(uuid, feedBackUpdate))
                .build();

    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<Void> deleteFeedback(@PathVariable String uuid) {

        feedBackService.deleteFeedBack(uuid);

        return BaseRestResponse.<Void>builder()
                .message("Delete feedback")
                .build();

    }


    @Operation(summary = "Get feedback details")
    @GetMapping("/details/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FeedbackResponseDetails> getFeedbackDetails(@PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponseDetails>builder()
                .message("Get feedback details")
                .data(feedBackService.getFeedBackDetails(uuid))
                .build();

    }

    @Operation(summary = "Get all feedback")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<FeedbackResponse>> getAllFeedbacks() {

        return BaseRestResponse.<List<FeedbackResponse>>builder()
                .message("Get all feedback")
                .data(feedBackService.getAllFeedbacks())
                .build();

    }



}
