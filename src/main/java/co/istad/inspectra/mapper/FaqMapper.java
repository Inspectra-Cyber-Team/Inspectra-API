package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Faq;
import co.istad.inspectra.features.faq.dto.FaqRequest;
import co.istad.inspectra.features.faq.dto.FaqResponse;
import co.istad.inspectra.features.faq.dto.FaqUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FaqMapper {

    Faq mapToFaq(FaqRequest faqRequest);

    FaqResponse mapToFaqResponse(Faq faq);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateFaqFromRequest(@MappingTarget Faq faq, FaqUpdateRequest faqUpdateRequest);

}
