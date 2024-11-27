package co.istad.inspectra.features.sonaruser;

import co.istad.inspectra.base.SonaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SonarServiceImpl implements SonarUserService {

    private final SonaResponse sonaResponse;

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${sonar.token}")
    private String sonarUserToken;

    @Override
    public Object getSonarUser() throws Exception {

        String url = sonarUrl + "/api/users/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sonarUserToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
    }
}
