package co.istad.inspectra.features.scan.next;

import co.istad.inspectra.features.scan.next.dto.NextScanningRequest;

public interface NextService {

    String nextScanning(NextScanningRequest nextScanningRequest) throws Exception;



}
