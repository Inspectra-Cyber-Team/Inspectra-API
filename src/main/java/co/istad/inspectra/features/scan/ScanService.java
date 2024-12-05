package co.istad.inspectra.features.scan;

import co.istad.inspectra.features.scan.dto.ScanningRequestDto;

/**
 * Service for scanning projects
 * @version 1.0
 * @author Lyhou
 * @see ScanServiceImpl
 */
public interface ScanService {

    /**
     * Scan project
     * @param scanningRequestDto ScanningRequestDto
     * @return String
     * @see ScanningRequestDto
     */
    String scanProject (ScanningRequestDto scanningRequestDto) throws Exception;


}
