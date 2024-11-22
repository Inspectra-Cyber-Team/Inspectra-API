package co.istad.inspectra.features.authority;

import co.istad.inspectra.domain.Authority;
import co.istad.inspectra.features.authority.dto.AuthorityRequest;
import co.istad.inspectra.features.authority.dto.AuthorityResponse;
import co.istad.inspectra.mapper.AuthorityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;
    @Override
    public Page<AuthorityResponse> findAll(int page, int limit) {

        //check if page and limit are greater than 0
        if(page < 0 || limit < 0){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Page and limit must be greater than 0"
            );
        }

        return authorityRepository.findAll
                (PageRequest.of(page, limit))
                .map(authorityMapper::authorityToResponse);

    }

    @Override
    public AuthorityResponse findById(String uuid) {

        //check if authority exists by uuid
        Authority authority = authorityRepository.findByUuid(uuid).

                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Authority with uuid = %s have been not found", uuid)
                )
        );

        return authorityMapper.authorityToResponse(authority);
    }

    @Override
    public AuthorityResponse update(String uuid, AuthorityRequest authorityRequest) {

        //check if authority exists by uuid
        Authority authority = authorityRepository.findByUuid(uuid).

                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Authority with uuid = %s  have been not found", uuid)
                )
        );

        authority.setAuthorityName(authorityRequest.authorityName());
        authority.setDescription(authorityRequest.description());
        authorityRepository.save(authority);

        return authorityMapper.authorityToResponse(authority);
    }

    @Override
    public AuthorityResponse create(AuthorityRequest authorityRequest) {

        //handle if authority already exists
        if (authorityRepository.existsByAuthorityName(authorityRequest.authorityName())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("Authority with name = %s already exists", authorityRequest.authorityName())
            );
        }

        Authority authority = authorityMapper.toAuthorityRequest(authorityRequest);
        authority.setUuid(UUID.randomUUID().toString());

        return authorityMapper.authorityToResponse(authorityRepository.save(authority));
    }

    @Override
    public void delete(String uuid) {

        //check if authority exists by uuid
       Authority authority = authorityRepository.findByUuid(uuid).
                orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Authority with uuid = %s have been not found", uuid)
                )
        );

        authorityRepository.delete(authority);

    }
}
