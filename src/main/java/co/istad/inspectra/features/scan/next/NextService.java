package co.istad.inspectra.features.scan.next;

import co.istad.inspectra.features.scan.dto.ScanningRequestDto;


public interface NextService {

    String nextScanning(ScanningRequestDto scanningRequestDto) throws Exception;



}
