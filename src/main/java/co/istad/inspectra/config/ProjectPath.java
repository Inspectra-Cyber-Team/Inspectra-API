package co.istad.inspectra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor

public class ProjectPath {

    @Value("${git.clone_directory}")
    private String clone_dir;

    private final GitConfig gitConfig;

    private final AppConfig appConfig;
    public String getProjectPath(String gitUrl, String branch) {

        String currentDir = appConfig.getProjectAbsolutePath();

        String cloneProject = currentDir + clone_dir;

        String projectName = gitConfig.gitClone(gitUrl,branch,cloneProject,"","");

        return cloneProject + projectName;
    }


}
