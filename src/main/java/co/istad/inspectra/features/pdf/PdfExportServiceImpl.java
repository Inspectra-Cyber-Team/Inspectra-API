package co.istad.inspectra.features.pdf;

import co.istad.inspectra.features.issue.IssueService;
import co.istad.inspectra.features.project_analy.ProjectAnalyzeService;
import com.lowagie.text.PageSize;
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


@Service
@RequiredArgsConstructor

public class PdfExportServiceImpl implements PdfExportService {

    private final TemplateEngine templateEngine;

    private final IssueService issueService;

    private final ProjectAnalyzeService projectAnalyzeService;
    public ResponseEntity<byte[]> generatePdf(String projectName) throws IOException {
        try {

            var issues = issueService.getIssueByProjectNameFlux(projectName).collectList().block();

            var lines = projectAnalyzeService.getLines(projectName).collectList().block();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Context context = new Context();
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