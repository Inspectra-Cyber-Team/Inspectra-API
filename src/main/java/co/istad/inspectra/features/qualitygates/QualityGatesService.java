package co.istad.inspectra.features.qualitygates;

public interface QualityGatesService {

    Object getQualityGatesByProjectName(String projectName) throws Exception;

    Object getAllQualityGates() throws Exception;

    Object CustomScan(String projectName,String qualityGate) throws Exception;


}
