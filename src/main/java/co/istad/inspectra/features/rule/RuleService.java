package co.istad.inspectra.features.rule;


import co.istad.inspectra.features.rule.dto.RuleLanguageCountResponse;
import co.istad.inspectra.features.rule.dto.RulesResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RuleService {

    String getRuleDetails(String roleKey) throws Exception;

    Mono<Object> getRuleList(int page,int pageSize,String language,String sortBy, String query) throws Exception;

    Flux<RulesResponseDto> getRuleDetails1(String ruleKey);

    Mono<Object> getAllLanguages() throws Exception;

    Mono<Object> getRulesRepository() throws Exception;

    Mono<Object> getRuleLanguageCount() throws Exception;


    Mono<List<RuleLanguageCountResponse>> getRuleLanguageCount1() throws Exception;



}
