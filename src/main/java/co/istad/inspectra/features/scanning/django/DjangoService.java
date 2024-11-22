package co.istad.inspectra.features.scanning.django;

import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;

public interface DjangoService {

    String scanDjango(ScanningRequestDto scanningRequestDto);

}
