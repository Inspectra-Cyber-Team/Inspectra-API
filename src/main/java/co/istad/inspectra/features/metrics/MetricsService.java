package co.istad.inspectra.features.metrics;

public interface MetricsService {




    Object getMetricsList() throws Exception;

    Object getMetricsDetails(String metricKey) throws Exception;
}
