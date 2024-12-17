package co.istad.inspectra.features.feedback;

import co.istad.inspectra.domain.Feedback;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.feedback.dto.FeedBackUpdate;
import co.istad.inspectra.features.feedback.dto.FeedbackRequest;
import co.istad.inspectra.features.feedback.dto.FeedbackResponse;
import co.istad.inspectra.features.feedback.dto.FeedbackResponseDetails;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.FeedbackMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class FeedBackServiceImpl implements FeedBackService{

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final UserRepository userRepository;


    @Override
    public FeedbackResponse getFeedBackByUuid(String uuid) {

        Feedback feedback = findProjectByUuid(uuid);

        return feedbackMapper.toFeedbackResponse(feedback);

    }

    @Override
    public Page<FeedbackResponse> getFeedBacks(int page, int limit) {

       if (page < 0 || limit < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and limit must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        Page<Feedback> feedbacks = feedbackRepository.findAll(pageRequest);

        return feedbacks.map(feedbackMapper::toFeedbackResponse);

    }

    @Override
    public FeedbackResponse createFeedBack(@AuthenticationPrincipal CustomUserDetails customUserDetails, FeedbackRequest request) {

        if (customUserDetails == null ) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        String email = customUserDetails.getUsername();

        User user = userRepository.findUsersByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Feedback feedback = feedbackMapper.toFeedback(request);

        feedback.setEmail(email);

        feedback.setUuid(UUID.randomUUID().toString());

        feedback.setUser(user);

        feedbackRepository.save(feedback);

        return feedbackMapper.toFeedbackResponse(feedback);
    }

    @Override
    public FeedbackResponse updateFeedBack(String uuid, FeedBackUpdate feedBackUpdate) {

        Feedback feedback = findProjectByUuid(uuid);

        feedback.setMessage(feedBackUpdate.message());

        feedbackRepository.save(feedback);

        return feedbackMapper.toFeedbackResponse(feedback);
    }

    @Override
    public void deleteFeedBack(String uuid) {

        Feedback feedback = findProjectByUuid(uuid);

        feedbackRepository.delete(feedback);

    }

    @Override
    public FeedbackResponseDetails getFeedBackDetails(String uuid) {

        Feedback feedback = findProjectByUuid(uuid);

        return feedbackMapper.toFeedbackResponseDetails(feedback);

    }

    @Override
    public List<FeedbackResponse> getAllFeedbacks() {

        List<Feedback> feedbacks = feedbackRepository.findAll();

        return feedbacks.stream()
                .map(feedbackMapper::toFeedbackResponse)
                .toList();

    }

    @Override
    public int countAllFeedbacks() {

        if (feedbackRepository.findAll().isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback is empty");
        }

        return feedbackRepository.findAll().size();


    }


    private Feedback findProjectByUuid(String uuid){

        return feedbackRepository.findByUuid(uuid).

                          orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Feedback not found  with uuid: "+uuid));

    }



}
