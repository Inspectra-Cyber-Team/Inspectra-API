package co.istad.inspectra.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Method get request headers for sonar
 *
 * @Author : lyhou
 * @since : 1.0 (2024)
 *
 */
@Service
public class SonarHeadersUtil {

    @Value("${sonar.token}")
    private String sonarToken;

    public HttpHeaders getSonarHeader(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sonarToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return  headers;

    }

}
