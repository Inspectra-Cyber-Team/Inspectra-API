package co.istad.inspectra.features.metrics;

import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricsService metricsService;

    @GetMapping("/list")
    public BaseRestResponse<Object> getMetricsList() throws Exception {
        return BaseRestResponse
                .builder()
                .data(metricsService.getMetricsList())
                .message("Metrics list has been fetched successfully.")
                .build();

    }

    @GetMapping("/details")
    public BaseRestResponse<Object> getMetricsDetails(String metricKey) throws Exception {

        return BaseRestResponse
                .builder()
                .data(metricsService.getMetricsDetails(metricKey))
                .message("Metrics details has been fetched successfully.")
                .build();

    }
    


}
