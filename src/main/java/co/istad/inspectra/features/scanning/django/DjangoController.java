package co.istad.inspectra.features.scanning.django;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/django")
@RequiredArgsConstructor

public class DjangoController {

    private final DjangoService djangoService;

    @PostMapping("/scan")
    public BaseRestResponse<String> scanDjango(@Valid @RequestBody ScanningRequestDto scanningRequestDto) {

        return BaseRestResponse.<String>builder()
                .data(djangoService.scanDjango(scanningRequestDto))
                .message("Scanning Django project successfully")
                .build();


    }


}
