package co.istad.inspectra.features.scanning.next;

import co.istad.inspectra.features.scanning.next.dto.NextScanningRequest;

public interface NextService {

    String nextScanning(NextScanningRequest nextScanningRequest) throws Exception;



}
