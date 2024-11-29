package co.istad.inspectra.features.test1;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Service
//public class SonarQubeService {
//
//    private final WebClient webClient;
//    private final String sonarUrl = "http://your-sonarqube-url"; // replace with your actual SonarQube URL
//    private final String sonarToken = "your-sonarqube-token"; // replace with your actual SonarQube token
//
//    public SonarQubeService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl(sonarUrl).build();
//    }
//
//
//    public Flux<ProjectGrade> getProjectOverview(String projectName) throws Exception {
//
//        return webClient.get()
//                .uri(sonarUrl + "/api/measures/component?component=" + projectName + "&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density")
//                .headers(headers -> headers.setBearerAuth(sonarToken))
//                .retrieve()
//                .bodyToMono(SonarApiResponse.class)  // Deserialize the response into a SonarApiResponse object
//                .flatMapMany(response -> {
//                    // Extract the measures from the API response
//                    List<Measure> measures = response.getComponent().getMeasures();
//                    Metrics metrics = extractMetrics(measures);
//
//                    // Calculate the grades based on the metrics
//                    ProjectGrade grade = calculateGrade(metrics);
//
//                    // Return the result
//                    return Flux.just(grade);
//                })
//                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));
//    }
//
//    // Extract the necessary metrics from the measures
//    private Metrics extractMetrics(List<Measure> measures) {
//        Metrics metrics = new Metrics();
//        for (Measure measure : measures) {
//            switch (measure.getMetric()) {
//                case "vulnerabilities":
//                    metrics.setVulnerabilities(Integer.parseInt(measure.getValue()));
//                    break;
//                case "maintainability_issues":
//                    metrics.setMaintainabilityIssuesTotal(extractTotalIssues(measure.getValue()));
//                    break;
//                case "bugs":
//                    metrics.setBugs(Integer.parseInt(measure.getValue()));
//                    break;
//                case "code_smells":
//                    metrics.setCodeSmells(Integer.parseInt(measure.getValue()));
//                    break;
//                case "duplicated_lines_density":
//                    metrics.setDuplicatedLinesDensity(Double.parseDouble(measure.getValue()));
//                    break;
//                case "coverage":
//                    metrics.setCoverage(Double.parseDouble(measure.getValue()));
//                    break;
//                // Add other metrics as needed
//            }
//        }
//        return metrics;
//    }
//
//    // Extract the total number of issues from a JSON-like string for maintainability and reliability issues
//    private int extractTotalIssues(String value) {
//        // Example: "{\"INFO\":0,\"LOW\":141,\"MEDIUM\":70,\"HIGH\":0,\"BLOCKER\":0,\"total\":211}"
//        String totalStr = value.replaceAll(".*\"total\":([0-9]+).*", "$1");
//        return Integer.parseInt(totalStr);
//    }
//
//    // Calculate the grade based on the metrics
//    private ProjectGrade calculateGrade(Metrics metrics) {
//        ProjectGrade grade = new ProjectGrade();
//        grade.setSecurityGrade(calculateMetricGrade(metrics.getVulnerabilities(), new int[]{0, 2, 5, 10}, new String[]{"A", "B", "C", "D", "E"}));
//        grade.setMaintainabilityGrade(calculateMetricGrade(metrics.getMaintainabilityIssuesTotal(), new int[]{50, 100, 150, 200}, new String[]{"A", "B", "C", "D", "E"}));
//        grade.setBugsGrade(calculateMetricGrade(metrics.getBugs(), new int[]{3, 5, 10, 15}, new String[]{"A", "B", "C", "D", "E"}));
//        grade.setCodeSmellsGrade(calculateMetricGrade(metrics.getCodeSmells(), new int[]{50, 100, 150, 200}, new String[]{"A", "B", "C", "D", "E"}));
//        grade.setDuplicatedLinesGrade(calculateMetricGrade(metrics.getDuplicatedLinesDensity(), new double[]{5, 10, 15, 20}, new String[]{"A", "B", "C", "D", "E"}));
//        grade.setCoverageGrade(calculateMetricGrade(metrics.getCoverage(), new double[]{80, 60, 40, 20}, new String[]{"A", "B", "C", "D", "E"}));
//
//        // Calculate the overall grade
//        String overallGrade = calculateOverallGrade(grade);
//        grade.setOverallGrade(overallGrade);
//
//        return grade;
//    }
//
//    // Helper method to calculate grade based on metric value (for integers)
//    private String calculateMetricGrade(int value, int[] thresholds, String[] scale) {
//        for (int i = 0; i < thresholds.length; i++) {
//            if (value <= thresholds[i]) {
//                return scale[i];
//            }
//        }
//        return scale[scale.length - 1];  // Worst grade if value exceeds all thresholds
//    }
//
//    // Helper method to calculate grade based on metric value (for doubles)
//    private String calculateMetricGrade(double value, double[] thresholds, String[] scale) {
//        for (int i = 0; i < thresholds.length; i++) {
//            if (value <= thresholds[i]) {
//                return scale[i];
//            }
//        }
//        return scale[scale.length - 1];  // Worst grade if value exceeds all thresholds
//    }
//
//    // Helper method to calculate overall grade by averaging individual grades
//    private String calculateOverallGrade(ProjectGrade grade) {
//        // Convert grades to numeric values: A=1, B=2, C=3, D=4, E=5
//        int total = 0;
//        int count = 0;
//
//        total += gradeToValue(grade.getSecurityGrade());
//        total += gradeToValue(grade.getMaintainabilityGrade());
//        total += gradeToValue(grade.getBugsGrade());
//        total += gradeToValue(grade.getCodeSmellsGrade());
//        total += gradeToValue(grade.getDuplicatedLinesGrade());
//        total += gradeToValue(grade.getCoverageGrade());
//
//        count = 6; // Number of metrics
//
//        int average = total / count;
//
//        return valueToGrade(average);
//    }
//
//    // Helper method to map grade letters to numeric values
//    private int gradeToValue(String grade) {
//        switch (grade) {
//            case "A": return 1;
//            case "B": return 2;
//            case "C": return 3;
//            case "D": return 4;
//            case "E": return 5;
//            default: return 5;  // default to E if invalid grade
//        }
//    }
//
//    // Helper method to map numeric average to grade letter
//    private String valueToGrade(int value) {
//        switch (value) {
//            case 1: return "A";
//            case 2: return "B";
//            case 3: return "C";
//            case 4: return "D";
//            case 5: return "E";
//            default: return "E";
//        }
//    }
//
//    // Define a class for the response structure
//    public static class SonarApiResponse {
//        private Component component;
//
//        public Component getComponent() {
//            return component;
//        }
//
//        public void setComponent(Component component) {
//            this.component = component;
//        }
//    }
//
//    public static class Component {
//        private List<Measure> measures;
//
//        public List<Measure> getMeasures() {
//            return measures;
//        }
//
//        public void setMeasures(List<Measure> measures) {
//            this.measures = measures;
//        }
//    }
//
//    public static class Measure {
//        private String metric;
//        private String value;
//        private boolean bestValue;
//
//        public String getMetric() {
//            return metric;
//        }
//
//        public void setMetric(String metric) {
//            this.metric = metric;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public boolean isBestValue() {
//            return bestValue;
//        }
//
//        public void setBestValue(boolean bestValue) {
//            this.bestValue = bestValue;
//        }
//    }
//
//    // Define a class to hold the metrics values
//    public static class Metrics {
//        private int vulnerabilities;
//        private int maintainabilityIssuesTotal;
//        private int bugs;
//        private int codeSmells;
//        private double duplicatedLinesDensity;
//        private double coverage;
//
//        public int getVulnerabilities() {
//            return vulnerabilities;
//        }
//
//        public void setVulnerabilities(int vulnerabilities) {
//            this.vulnerabilities = vulnerabilities;
//        }
//
//        public int getMaintainabilityIssuesTotal() {
//            return maintainabilityIssuesTotal;
//        }
//
//        public void setMaintainabilityIssuesTotal(int maintainabilityIssuesTotal) {
//            this.maintainabilityIssuesTotal = maintainabilityIssuesTotal;
//        }
//
//        public int getBugs() {
//            return bugs;
//        }
//
//        public void setBugs(int bugs) {
//            this.bugs = bugs;
//        }
//
//        public int getCodeSmells() {
//            return codeSmells;
//        }
//
//        public void setCodeSmells(int codeSmells) {
//            this.codeSmells = codeSmells;
//        }
//
//        public double getDuplicatedLinesDensity() {
//            return duplicatedLinesDensity;
//        }
//
//        public void setDuplicatedLinesDensity(double duplicatedLinesDensity) {
//            this.duplicatedLinesDensity = duplicatedLinesDensity;
//        }
//
//        public double getCoverage() {
//            return coverage;
//        }
//
//        public void setCoverage(double coverage) {
//            this.coverage = coverage;
//        }
//    }
//
//    // Define a class to hold the calculated grades
//    public static class ProjectGrade {
//        private String securityGrade;
//        private String maintainabilityGrade;
//        private String bugsGrade;
//        private String codeSmellsGrade;
//        private String duplicatedLinesGrade;
//        private String coverageGrade;
//        private String overallGrade;
//
//        public String getSecurityGrade() {
//            return securityGrade;
//        }
//
//        public void setSecurityGrade(String securityGrade) {
//            this.securityGrade = securityGrade;
//        }
//
//        public String getMaintainabilityGrade() {
//            return maintainabilityGrade;
//        }
//
//        public void setMaintainabilityGrade(String maintainabilityGrade) {
//            this.maintainabilityGrade = maintainabilityGrade;
//        }
//
//        public String getBugsGrade() {
//            return bugsGrade;
//        }
//
//        public void setBugsGrade(String bugsGrade) {
//            this.bugsGrade = bugsGrade;
//        }
//
//        public String getCodeSmellsGrade() {
//            return codeSmellsGrade;
//        }
//
//        public void setCodeSmellsGrade(String codeSmellsGrade) {
//            this.codeSmellsGrade = codeSmellsGrade;
//        }
//
//        public String getDuplicatedLinesGrade() {
//            return duplicatedLinesGrade;
//        }
//
//        public void setDuplicatedLinesGrade(String duplicatedLinesGrade) {
//            this.duplicatedLinesGrade = duplicatedLinesGrade;
//        }
//
//        public String getCoverageGrade() {
//            return coverageGrade;
//        }
//
//        public void setCoverageGrade(String coverageGrade) {
//            this.coverageGrade = coverageGrade;
//        }
//
//        public String getOverallGrade() {
//            return overallGrade;
//        }
//
//        public void setOverallGrade(String overallGrade) {
//            this.overallGrade = overallGrade;
//        }
//    }
//
//}


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SonarQubeService {


    public ResponseEntity<String> createProject (HttpServletRequest request)
    {

        String token = (String) request.getAttribute("ANONYMOUS_TOKEN");

        if (token == null) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Anonymous token not found");

        }

        return ResponseEntity.ok("Project created for token" + token);

    }

}

