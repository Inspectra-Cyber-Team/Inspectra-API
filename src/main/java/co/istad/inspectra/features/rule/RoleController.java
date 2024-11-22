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
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RuleService ruleService;

    @GetMapping("/rule")
    public Mono<Object> getAllRules(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize,
                                    @RequestParam(required = false) String language, @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) String query) throws Exception {

        return ruleService.getRuleList(page, pageSize, language, sortBy, query);

    }


    @GetMapping("/rule/{roleKey}")
    public String getRuleDetails(@PathVariable String roleKey) throws Exception {

        return ruleService.getRuleDetails(roleKey);

    }


    @GetMapping("/rule1/{ruleKey}")
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
    @GetMapping("/rules-repository")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getRulesRepository() throws Exception {

        return ruleService.getRulesRepository();

    }


    @Operation(summary = "Get rule language count")
    @GetMapping("/rule-language-count")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Object> getRuleLanguageCount() throws Exception {

        return ruleService.getRuleLanguageCount();

    }

    @Operation(summary = "Get rule language count")
    @GetMapping("/rule-language-count1")
    @ResponseStatus(HttpStatus.OK)
    public Mono<List<RuleLanguageCountResponse>> getRuleLanguageCount1() throws Exception {

        return ruleService.getRuleLanguageCount1();

    }


}



