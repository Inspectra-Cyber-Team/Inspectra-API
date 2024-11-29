package co.istad.inspectra.features.userrole;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.userrole.dto.UserRoleRequest;
import co.istad.inspectra.features.userrole.dto.UserRoleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user_role")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<UserRoleResponse>> getAllUserRoles(){

        return BaseRestResponse
                .<List<UserRoleResponse>>builder()
                .data(userRoleService.getAllUserRoles())
                .build();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<UserRoleResponse> createRole(@Valid @RequestBody UserRoleRequest userRoleRequest){

        return BaseRestResponse
                .<UserRoleResponse>builder()
                .data(userRoleService.createRole(userRoleRequest))
                .build();
    }

    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<UserRoleResponse> getRole(@PathVariable String uuid){

        return BaseRestResponse
                .<UserRoleResponse>builder()
                .data(userRoleService.getRole(uuid))
                .build();
    }


    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<UserRoleResponse> updateRole(@PathVariable String uuid, @Valid @RequestBody UserRoleRequest userRoleRequest){

        return BaseRestResponse
                .<UserRoleResponse>builder()
                .data(userRoleService.updateRole(uuid, userRoleRequest))
                .build();
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable String uuid){

        userRoleService.deleteRole(uuid);

    }



}
