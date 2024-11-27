package co.istad.inspectra.config;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class GitConfig {

    public String gitClone(String url, String branch, String cloneDir, String accessToken, String languageName)
    {


        if(url == null || url.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Git URL is required");
        }

        if(branch == null || branch.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Branch is required");
        }

        if(cloneDir == null || cloneDir.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Clone directory is required");
        }

        String projectName = languageName + UUID.randomUUID();


        File dir = new File(cloneDir + projectName);

        if (!dir.exists()) {
            dir.mkdir();
        }

        try {

            CloneCommand git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(dir)
                    .setBranch(branch);

            if (accessToken != null && !accessToken.isEmpty()) {

                git.setCredentialsProvider(new UsernamePasswordCredentialsProvider(accessToken, ""));

            }

            git.call();
            return projectName;

        } catch (GitAPIException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }



    }

}
