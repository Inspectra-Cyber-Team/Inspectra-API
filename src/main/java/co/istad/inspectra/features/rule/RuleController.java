package co.istad.inspectra.features.rule;

import co.istad.inspectra.features.rule.dto.RuleLanguageCountResponse;
import co.istad.inspectra.features.rule.dto.RulesResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @GetMapping()
    public Mono<Object> getAllRules(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize
                                 ) throws Exception {

        return ruleService.getRuleList(page, pageSize);

    }

    @GetMapping("{ruleKey}")
    public Flux<RulesResponseDto> getRuleDetails1(@PathVariable String ruleKey) {

        return ruleService.getRuleDetails1(ruleKey);

    }

    @Operation(summary = "Get all languages")
    @GetMapping("/languages")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getAllLanguages() throws Exception {

        return ruleService.getAllLanguages();

    }


    @Operation(summary = "Get all rules repository")
    @GetMapping("/rules_repository")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getRulesRepository() throws Exception {

        return ruleService.getRulesRepository();

    }


    @Operation(summary = "Get rule language count")
    @GetMapping("/rule_language_count")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getRuleLanguageCount() throws Exception {

        return ruleService.getRuleLanguageCount();

    }



}



