package co.istad.inspectra.features.faq;

import co.istad.inspectra.domain.Faq;
import co.istad.inspectra.features.faq.dto.FaqRequest;
import co.istad.inspectra.features.faq.dto.FaqResponse;
import co.istad.inspectra.features.faq.dto.FaqUpdateRequest;
import co.istad.inspectra.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    private final FaqMapper faqMapper;

    @Override
    public List<FaqResponse> getAllFaq() {

        return faqRepository.findAll().stream()
                .map(faqMapper::mapToFaqResponse)
                .toList();

    }

    @Override
    public Page<FaqResponse> getAllFaqByPagination(int page, int size) {

        if(page < 0 || size < 0)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Faq> faqPage = faqRepository.findAll(pageRequest);

        return faqPage.map(faqMapper::mapToFaqResponse);
    }


    @Override
    public FaqResponse getFaqByUuid(String uuid) {

        Faq faq = faqRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found with UUID: " + uuid));

        return faqMapper.mapToFaqResponse(faq);

    }

    @Override
    public FaqResponse getFaqByQuestion(String question) {

        Faq faq = faqRepository.findByQuestion(question).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found with question: " + question));

        return faqMapper.mapToFaqResponse(faq);

    }

    @Override
    public FaqResponse getFaqByAnswer(String answer) {

        Faq faq = faqRepository.findByAnswer(answer).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found with answer: " + answer));

        return faqMapper.mapToFaqResponse(faq);

    }

    @Override
    public FaqResponse createFaq(FaqRequest faqRequest) {

        Faq faq = faqMapper.mapToFaq(faqRequest);
        faq.setUuid(UUID.randomUUID().toString());

        return faqMapper.mapToFaqResponse(faqRepository.save(faq));

    }

    @Override
    public FaqResponse updateFaq(FaqUpdateRequest faqUpdateRequest, String uuid) {

        Faq faq = faqRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found with UUID: " + uuid));

        faqMapper.updateFaqFromRequest(faq, faqUpdateRequest);

        return faqMapper.mapToFaqResponse(faqRepository.save(faq));
    }

    @Override
    public void deleteFaq(String uuid) {

        Faq faq = faqRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faq not found with UUID: " + uuid));

        faqRepository.delete(faq);

    }
}
