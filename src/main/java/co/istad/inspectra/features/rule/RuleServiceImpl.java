package co.istad.inspectra.features.rule;

import co.istad.inspectra.features.rule.dto.RuleLanguageCountResponse;
import co.istad.inspectra.features.rule.dto.RulesResponseDto;
import co.istad.inspectra.features.rule.dto.RulesWrapperResponse;
import co.istad.inspectra.utils.SonarHeadersUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService{

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${sonar.token}")
    private String sonarToken;

    private final RestTemplate restTemplate;

    private final SonarHeadersUtil sonarHeadersUtil;

    private final WebClient webClient;


    @Override
    public Object getRuleDetails(String roleKey) {

        if (roleKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rule key is required");
        }

        String url = sonarUrl + "/api/rules/show?key=" + roleKey;

        HttpHeaders headers = sonarHeadersUtil.getSonarHeader();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class).getBody();


    }

    @Override
    public Mono<Object> getRuleList(int page,int pageSize) throws Exception {

        if (page <0 || pageSize < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and page size must be greater than 0");
        }

       return webClient.get()
                .uri(sonarUrl + "/api/rules/search?ps=" + pageSize + "&p=" + page)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });


    }

    @Override
    public Flux<RulesResponseDto> getRuleDetails1(String ruleKey) {

        if (ruleKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rule key is required");
        }

        String url = sonarUrl + "/api/rules/show?key=" + ruleKey ;

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(RulesWrapperResponse.class)
                .flatMapMany(rulesWrapperResponse -> Flux.just(rulesWrapperResponse.rule()))
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });

    }

    @Override
    public Mono<Object> getAllLanguages() throws Exception {

        String url = sonarUrl + "/api/languages/list";

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });
    }

    @Override
    public Mono<Object> getRulesRepository() throws Exception {

        String url = sonarUrl + "/api/rules/repositories";

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });


    }

    @Override
    public Mono<Object> getRuleLanguageCount() throws Exception {


        String url = sonarUrl + "/api/qualityprofiles/search";

        //can we check to count rule follow type of language
        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });
    }




}
