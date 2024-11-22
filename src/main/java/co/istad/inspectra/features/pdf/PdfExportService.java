package co.istad.inspectra.features.pdf;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public interface PdfExportService {

     ResponseEntity<byte[]> generatePdf(String projectName) throws IOException;


}
