package co.istad.inspectra.features.scan.react;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scans/react")
@RequiredArgsConstructor

public class ReactController {

    private final ReactService reactService;


    @PostMapping
    public BaseRestResponse<String> scanningReact(@Valid @RequestBody ScanningRequestDto scanningRequestDto) {

      return BaseRestResponse.<String>builder()
                .data(reactService.scanningReact(scanningRequestDto))
                .message("Scanning React successfully")
                .build();


    }


}
