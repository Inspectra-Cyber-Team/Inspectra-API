package co.istad.inspectra.features.qualitygates;

import co.istad.inspectra.base.SonaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QualityGatesServiceImpl implements QualityGatesService {

    @Value("${sonar.url}")
    private String sonarUrl;
    @Value("${sonar.token}")
    private String sonarUserToken;

    private final SonaResponse sonaResponse;

    @Override
    public Object getQualityGatesByProjectName(String projectName) throws Exception {

        String url = sonarUrl + "/api/qualitygates/project_status?projectKey=" + projectName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sonarUserToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
    }

    @Override
    public Object getAllQualityGates() throws Exception {

        String url = sonarUrl + "/api/qualitygates/list";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sonarUserToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);

    }


}
