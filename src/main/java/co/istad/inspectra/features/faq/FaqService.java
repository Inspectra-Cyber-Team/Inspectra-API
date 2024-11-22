package co.istad.inspectra.features.faq;

import co.istad.inspectra.features.faq.dto.FaqRequest;
import co.istad.inspectra.features.faq.dto.FaqResponse;
import co.istad.inspectra.features.faq.dto.FaqUpdateRequest;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service for FAQ
 * @author : Lyhou
 * @since : (2024)
 * @version : 1.0
 *
 */
public interface FaqService {

    /**
     * Get all FAQ
     * @return {@link  java.util.List<FaqResponse>}
     */

    List<FaqResponse> getAllFaq();


    /**
     * Get all Faq by Pagination
     * @param page is the page number
     * @param size is the number of items in a page
     * @return {@link org.springframework.data.domain.Page}
     *
     */

    Page<FaqResponse> getAllFaqByPagination(int page, int size);



    /**
     * Get FAQ by UUID
     * @param uuid is the unique identifier of the FAQ
     * @return {@link  FaqResponse}
     */
    FaqResponse getFaqByUuid(String uuid);

    /**
     * Get FAQ by question
     * @param question is the question of the FAQ
     * @return {@link  FaqResponse}
     */
    FaqResponse getFaqByQuestion(String question);


    /**
     * Get FAQ by answer
     * @param answer is the answer of the FAQ
     * @return {@link  FaqResponse}
     */
    FaqResponse getFaqByAnswer(String answer);


    /**
     * Create a new FAQ
     * @param faqResponse is the FAQ to be created
     * @return {@link  FaqResponse}
     */
    FaqResponse createFaq(FaqRequest faqResponse);

    /**
     * Update a FAQ
     * @param faqUpdateRequest is the FAQ to be updated
     * @return {@link  FaqResponse}
     */
    FaqResponse updateFaq(FaqUpdateRequest faqUpdateRequest, String uuid);


    /**
     * Delete a FAQ
     * @param uuid is the unique identifier of the FAQ
     */
    void deleteFaq(String uuid);


}
