package co.istad.inspectra.features.scanning.nestjs;

import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;

public interface NestJsService {

    String scanNestJs(ScanningRequestDto scanningRequestDto);

}
