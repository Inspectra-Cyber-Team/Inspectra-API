package co.istad.inspectra.features.rule;


import co.istad.inspectra.features.rule.dto.RuleLanguageCountResponse;
import co.istad.inspectra.features.rule.dto.RulesResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RuleService {

    Object getRuleDetails(String roleKey) throws Exception;

    Mono<Object> getRuleList(int page,int pageSize) throws Exception;

    Flux<RulesResponseDto> getRuleDetails1(String ruleKey);

    Mono<Object> getAllLanguages() throws Exception;

    Mono<Object> getRulesRepository() throws Exception;

    Mono<Object> getRuleLanguageCount() throws Exception;





}
