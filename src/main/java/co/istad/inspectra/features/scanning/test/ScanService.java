package co.istad.inspectra.features.scanning.test;

import co.istad.inspectra.features.scanning.test.dto.AnalyzeRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;



public interface ScanService {

    ResponseEntity<String> analyzeRepository(AnalyzeRequest request) throws MessagingException;

    boolean checkForBuildFiles(String projectPath);

    String sendMessage(String email) throws MessagingException;




}
