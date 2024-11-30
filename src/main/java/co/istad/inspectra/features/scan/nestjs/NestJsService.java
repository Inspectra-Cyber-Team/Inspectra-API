package co.istad.inspectra.features.scan.nestjs;

import co.istad.inspectra.features.scan.dto.ScanningRequestDto;

public interface NestJsService {

    String scanNestJs(ScanningRequestDto scanningRequestDto);

}
