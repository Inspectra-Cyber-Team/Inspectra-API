package co.istad.inspectra.features.scan.django;

import co.istad.inspectra.features.scan.dto.ScanningRequestDto;

public interface DjangoService {

    String scanDjango(ScanningRequestDto scanningRequestDto);

}
