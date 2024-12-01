package co.istad.inspectra.features.faq;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.faq.dto.FaqRequest;
import co.istad.inspectra.features.faq.dto.FaqResponse;
import co.istad.inspectra.features.faq.dto.FaqUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor

public class FaqController {

    private final FaqService faqService;

    @Operation(summary = "Get all FAQ")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<FaqResponse>> getAllFaq()
    {
        return BaseRestResponse.<List<FaqResponse>>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.getAllFaq())
                .message("Success fetch all FAQ")
                .build();
    }


    @Operation(summary = "Get all Faq by Pagination")
    @GetMapping("/pagination")
    @ResponseStatus(HttpStatus.OK)
    public Page<FaqResponse> getAllFaqByPagination(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "25") int size)
    {
        return faqService.getAllFaqByPagination(page, size);

    }


    @Operation(summary = "Get FAQ by UUID")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FaqResponse> getFaqByUuid(@PathVariable String uuid)
    {
        return BaseRestResponse.<FaqResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.getFaqByUuid(uuid))
                .message("Success fetch FAQ by UUID")
                .build();
    }

    @Operation(summary = "Get FAQ by question")
    @GetMapping("/question/{question}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FaqResponse> getFaqByQuestion(@PathVariable String question)
    {
        return BaseRestResponse.<FaqResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.getFaqByQuestion(question))
                .message("Success fetch FAQ by question")
                .build();
    }


    @Operation(summary = "Get FAQ by answer")
    @GetMapping("/answer/{answer}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<FaqResponse> getFaqByAnswer(@PathVariable String answer)
    {
        return BaseRestResponse.<FaqResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.getFaqByAnswer(answer))
                .message("Success fetch FAQ by answer")
                .build();
    }


    @Operation(summary = "Create a new FAQ")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<FaqResponse> createFaq(@Valid @RequestBody FaqRequest faqRequest)
    {
        return BaseRestResponse.<FaqResponse>
                builder()
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.createFaq(faqRequest))
                .message("Success create FAQ")
                .build();
    }


    @Operation(summary = "Update a FAQ")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<FaqResponse> updateFaq(@Valid @RequestBody FaqUpdateRequest faqUpdateRequest, @PathVariable String uuid)
    {
        return BaseRestResponse.<FaqResponse>
                builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(faqService.updateFaq(faqUpdateRequest, uuid))
                .message("Success update FAQ")
                .build();
    }


    @Operation(summary = "Delete a FAQ")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<Void> deleteFaq(@PathVariable String uuid)
    {
        faqService.deleteFaq(uuid);
        return BaseRestResponse.<Void>
                builder()
                .status(HttpStatus.NO_CONTENT.value())
                .timestamp(LocalDateTime.now())
                .message("Success delete FAQ")
                .build();
    }



}
