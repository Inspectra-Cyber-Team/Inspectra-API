package co.istad.inspectra.features.scan.spring;

public interface SpringBootService {

    String springBootScanning(String gitUrl, String branch, String projectName) throws Exception;

    boolean checkForBuildFiles(String projectPath);



}