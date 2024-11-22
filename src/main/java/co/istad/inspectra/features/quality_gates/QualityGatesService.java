package co.istad.inspectra.features.quality_gates;

public interface QualityGatesService {

    Object getQualityGatesByProjectName(String projectName) throws Exception;

    Object getAllQualityGates() throws Exception;


}
