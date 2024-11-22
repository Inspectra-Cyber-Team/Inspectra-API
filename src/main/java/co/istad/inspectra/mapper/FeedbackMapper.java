package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Feedback;
import co.istad.inspectra.features.feedback.dto.FeedbackRequest;
import co.istad.inspectra.features.feedback.dto.FeedbackResponse;
import co.istad.inspectra.features.feedback.dto.FeedbackResponseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    Feedback toFeedback(FeedbackRequest feedbackRequest);


    @Mapping(target = "profile", source = "user.profile")
    FeedbackResponse toFeedbackResponse(Feedback feedback);

    @Mapping(target = "profile", source = "user.profile")
    FeedbackResponseDetails toFeedbackResponseDetails(Feedback feedback);


}
