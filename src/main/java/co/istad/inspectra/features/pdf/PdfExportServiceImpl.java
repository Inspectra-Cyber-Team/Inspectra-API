package co.istad.inspectra.features.pdf;

import co.istad.inspectra.features.issue.IssueService;
import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.projectanaly.ProjectAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class PdfExportServiceImpl implements PdfExportService {

    private final TemplateEngine templateEngine;

    private final IssueService issueService;

    private final ProjectAnalyzeService projectAnalyzeService;
    public ResponseEntity<byte[]> generatePdf(String projectName) throws IOException {
        try {

            var issues = issueService.getIssueByProjectNameFlux(projectName).collectList().block();

            assert issues != null;
            Map<String, Long> severityCounts = issues.stream()
                    .collect(Collectors.groupingBy(IssuesResponse::severity, Collectors.counting()));


            var lines = projectAnalyzeService.getLines(projectName).collectList().block();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Context context = new Context();

            context.setVariable("blockerCount", severityCounts.getOrDefault("BLOCKER", 0L));
            context.setVariable("criticalCount", severityCounts.getOrDefault("CRITICAL", 0L));
            context.setVariable("majorCount", severityCounts.getOrDefault("MAJOR", 0L));
            context.setVariable("minorCount", severityCounts.getOrDefault("MINOR", 0L));
            context.setVariable("infoCount", severityCounts.getOrDefault("INFO", 0L));


            context.setVariable("projectName", projectName);
            context.setVariable("createdAt", LocalDateTime.now());
            context.setVariable("issues",issues );
            context.setVariable("lines",lines);

            String htmlString = templateEngine.process("scan-results", context);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlString);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream);


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error generating PDF", e);
        }
    }


}