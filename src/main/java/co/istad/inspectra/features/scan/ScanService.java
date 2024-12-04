package co.istad.inspectra.features.scan;

import co.istad.inspectra.features.scan.dto.ScanningRequestDto;

public interface ScanService {

    String scanProject (ScanningRequestDto scanningRequestDto) throws Exception;

    boolean checkForBuildFiles(String projectPath);

}
