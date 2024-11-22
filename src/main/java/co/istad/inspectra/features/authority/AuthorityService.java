package co.istad.inspectra.features.authority;


import co.istad.inspectra.features.authority.dto.AuthorityRequest;
import co.istad.inspectra.features.authority.dto.AuthorityResponse;
import org.springframework.data.domain.Page;

public interface AuthorityService {

    Page<AuthorityResponse> findAll(int page, int limit);
    AuthorityResponse findById(String uuid);
    AuthorityResponse update(String uuid, AuthorityRequest authorityRequest);

    AuthorityResponse create(AuthorityRequest authorityRequest);
    void delete(String uuid);

}
