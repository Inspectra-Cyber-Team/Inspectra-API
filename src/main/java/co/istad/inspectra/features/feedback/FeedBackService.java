package co.istad.inspectra.features.feedback;

import co.istad.inspectra.features.feedback.dto.FeedBackUpdate;
import co.istad.inspectra.features.feedback.dto.FeedbackRequest;
import co.istad.inspectra.features.feedback.dto.FeedbackResponseDetails;
import co.istad.inspectra.features.feedback.dto.FeedbackResponse;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

/**
 * service for feedback
 * Author: lyhou
 * since: 2024
 */
public interface FeedBackService {


    /**
     * get feedback by uuid
     * @param uuid use for get feedback
     * @return {@link FeedbackResponse}
     */
    FeedbackResponse getFeedBackByUuid(String uuid);

    /**
     * get all feedback
     * @param page is the current page number
     * @param limit is the size of record per page
     * @return {@link Page<FeedbackResponse>}
     */
    Page<FeedbackResponse> getFeedBacks(int page, int limit);

    /**
     * create feedback
     * @param request is the request for create feedback
     * @return {@link FeedbackResponse}
     */
    FeedbackResponse createFeedBack(@AuthenticationPrincipal CustomUserDetails customUserDetails, FeedbackRequest request);

    /**
     * update feedback
     * @param uuid is the request for update feedback
     * @return {@link FeedbackResponse}
     */
    FeedbackResponse updateFeedBack(String uuid, FeedBackUpdate feedBackUpdate);

    /**
     * delete feedback
     * @param uuid is the id for delete feedback
     */
    void deleteFeedBack(String uuid);


    /**
     *
     * get feedback details
     * @param uuid is the id for get feedback details
     * @return {@link FeedbackResponseDetails}
     *
     *
     */

    FeedbackResponseDetails getFeedBackDetails(String uuid);

    /**
     * get all feedbacks
     * @return {@link List<FeedbackResponse>}
     */
    List<FeedbackResponse> getAllFeedbacks();


    /**
     * count all feedbacks
     * @return {@link int}
     *
     */
    int countAllFeedbacks();


}
