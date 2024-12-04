package co.istad.inspectra.features.pdf;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("api/v1/pdf/")
@RequiredArgsConstructor

public class PdfExportController {

    private final PdfExportService pdfExportService;

    @GetMapping(value = "{projectName}", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<byte[]> generatePdf(@PathVariable String projectName) {
        try {

            return pdfExportService.generatePdf(projectName);

        } catch (IOException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error generating PDF", e);

        }
    }



}
