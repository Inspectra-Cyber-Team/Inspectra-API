package co.istad.inspectra.features.metrics;

import co.istad.inspectra.utils.SonarHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService{


    @Value("${sonar.url}")
    private String sonarUrl;
    @Value("${sonar.token}")
    private String sonarUserToken;

    private final RestTemplate restTemplate;

    private final SonarHeaders sonarHeaders;

    @Override
    public Object getMetricsList() throws Exception {

        String url = sonarUrl + "/api/metrics/search";

        HttpHeaders headers = sonarHeaders.getSonarHeader();

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();


    }

    @Override
    public Object getMetricsDetails(String metricKey) throws Exception {

        String url = sonarUrl + "/api/metrics/show?key=" + metricKey;

        HttpHeaders headers = sonarHeaders.getSonarHeader();

        HttpEntity<String> entity = new HttpEntity<>(headers);


        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

    }
}
