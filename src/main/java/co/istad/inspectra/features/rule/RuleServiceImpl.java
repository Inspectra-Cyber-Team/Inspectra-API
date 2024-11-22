package co.istad.inspectra.features.rule;

import co.istad.inspectra.features.rule.dto.RuleLanguageCountResponse;
import co.istad.inspectra.features.rule.dto.RulesResponseDto;
import co.istad.inspectra.features.rule.dto.RulesWrapperResponse;
import co.istad.inspectra.utils.SonarHeaders;
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

    private final SonarHeaders sonarHeaders;

    private final WebClient webClient;

    private final List<String> languages = List.of(
            "azureresourcemanager", "cs", "css", "cloudformation", "docker", "flex",
            "go", "web", "ipynb", "java", "js", "kotlin", "kubernetes",
            "php", "py", "ruby", "scala", "secrets", "terraform", "text", "ts", "vbnet",
            "xml"
    );



    @Override
    public String getRuleDetails(String roleKey) {

        String url = sonarUrl + "/api/rules/show?key=" + roleKey;

        HttpHeaders headers = sonarHeaders.getSonarHeader();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class).getBody();


    }

    @Override
    public Mono<Object> getRuleList(int page,int pageSize,String language,String sortBy, String query) throws Exception {

       return webClient.get()
                .uri(sonarUrl + "/api/rules/search?languages=" + language + "&p=" + page + "&ps=" + pageSize + "&s=" + sortBy + "&q=" + query)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });


    }

    @Override
    public Flux<RulesResponseDto> getRuleDetails1(String ruleKey) {

        String url = sonarUrl + "/api/rules/show?key=" + ruleKey ;
        System.out.println(url);

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToMono(RulesWrapperResponse.class)
                .doOnNext(response -> System.out.println("Response: " + response.toString()))
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



    @Override
    public Mono<List<RuleLanguageCountResponse>> getRuleLanguageCount1() throws Exception {

        Map<String, Integer> languageCount = new HashMap<>();

        languages.forEach(language -> {
            String url = sonarUrl + "/api/rules/search?languages=" + language;
            JsonNode response = webClient.get()
                    .uri(url)
                    .headers(headers -> headers.setBearerAuth(sonarToken))
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            languageCount.put(language, response.get("total").asInt());
        });

        return Mono.just(languageCount.entrySet().stream()
                .map(entry -> RuleLanguageCountResponse.builder()
                        .language(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .toList());


    }



}
