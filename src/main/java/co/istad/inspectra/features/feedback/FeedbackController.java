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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor

public class FeedbackController {

    private final FeedBackService feedBackService;

    @Operation(summary = "Get all feedback")
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<FeedbackResponse> getFeedbacks(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "25") int size) {

        return feedBackService.getFeedBacks(page, size);

    }


    @Operation(summary = "Get feedback by uuid")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FeedbackResponse> getFeedbackByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Get feedback by uuid")
                .data(feedBackService.getFeedBackByUuid(uuid))
                .build();

    }


    @Operation(summary = "Create feedback")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public BaseRestResponse<FeedbackResponse> createFeedback(@AuthenticationPrincipal CustomUserDetails customUserDetails,@Valid @RequestBody FeedbackRequest request) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .message("Create feedback")
                .data(feedBackService.createFeedBack(customUserDetails,request))
                .build();

    }

    @Operation(summary = "Update feedback")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
   @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<FeedbackResponse> updateFeedback(@Valid @RequestBody  FeedBackUpdate feedBackUpdate, @PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Update feedback")
                .data(feedBackService.updateFeedBack(uuid, feedBackUpdate))
                .build();

    }

    @Operation(summary = "Delete feedback")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
   @PreAuthorize("hasRole('ADMIN')")

    public BaseRestResponse<Void> deleteFeedback(@PathVariable String uuid) {

        feedBackService.deleteFeedBack(uuid);

        return BaseRestResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Delete feedback")
                .build();

    }


    @Operation(summary = "Get feedback details")
    @GetMapping("/details/{uuid}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<FeedbackResponseDetails> getFeedbackDetails(@PathVariable String uuid) {

        return BaseRestResponse.<FeedbackResponseDetails>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Get feedback details")
                .data(feedBackService.getFeedBackDetails(uuid))
                .build();

    }

    @Operation(summary = "Get all feedback")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ADMIN,USER')")
    public BaseRestResponse<List<FeedbackResponse>> getAllFeedbacks() {

        return BaseRestResponse.<List<FeedbackResponse>>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Get all feedback")
                .data(feedBackService.getAllFeedbacks())
                .build();

    }



}
