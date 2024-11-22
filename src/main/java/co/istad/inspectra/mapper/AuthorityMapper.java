package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Authority;
import co.istad.inspectra.features.authority.dto.AuthorityRequest;
import co.istad.inspectra.features.authority.dto.AuthorityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    // Authority toAuthorityRequest mapping
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "users", ignore = true)
    Authority toAuthorityRequest(AuthorityRequest authorityRequest);

    AuthorityResponse authorityToResponse(Authority authority);


}
