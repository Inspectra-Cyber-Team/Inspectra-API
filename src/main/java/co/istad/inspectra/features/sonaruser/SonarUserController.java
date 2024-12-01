package co.istad.inspectra.features.sonaruser;

import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping("/api/v1/sonar_user")
@RequiredArgsConstructor
@RestController
public class SonarUserController {

    private final SonarUserService sonarUserService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getSonarUser() throws Exception {
        return BaseRestResponse.builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(sonarUserService.getSonarUser())
                .message("Get sonar user.")
                .build();
    }


}
