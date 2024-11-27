package co.istad.inspectra.features.sonaruser;

import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/sonar_user")
@RequiredArgsConstructor
@RestController
public class SonarUserController {

    private final SonarUserService sonarUserService;

    @GetMapping("/user")
    public BaseRestResponse<Object> getSonarUser() throws Exception {
        return BaseRestResponse.builder()
                .data(sonarUserService.getSonarUser())
                .message("Get sonar user.")
                .build();
    }


}
