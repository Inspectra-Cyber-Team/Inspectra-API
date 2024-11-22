package co.istad.inspectra.features.authority;

import co.istad.inspectra.features.authority.dto.AuthorityRequest;
import co.istad.inspectra.features.authority.dto.AuthorityResponse;
import co.istad.inspectra.base.BaseRestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authority")
@RequiredArgsConstructor

public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping("/all")
    public Page<AuthorityResponse> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int limit) {

        return authorityService.findAll(page, limit);

    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<AuthorityResponse> findByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<AuthorityResponse>builder()
                .data(authorityService.findById(uuid))
                .message("Authority found")
                .build();

    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<AuthorityResponse> createAuthority(@Valid @RequestBody AuthorityRequest authorityRequest) {

        return BaseRestResponse.<AuthorityResponse>builder()
                .data(authorityService.create(authorityRequest))
                .message("Authority created")
                .build();

    }

    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<AuthorityResponse> updateAuthority(@PathVariable String uuid, @RequestBody AuthorityRequest authorityRequest) {

        return BaseRestResponse.<AuthorityResponse>builder()
                .data(authorityService.update(uuid, authorityRequest))
                .message("Authority updated")
                .build();

    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<String> deleteAuthority(@PathVariable String uuid) {

        authorityService.delete(uuid);
        return BaseRestResponse.<String>builder()
                .data("Authority deleted")
                .message("Authority deleted")
                .build();

    }

}
