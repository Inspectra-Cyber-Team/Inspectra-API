package co.istad.inspectra.config;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.eclipse.jgit.lib.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor

public class GitConfig {

    public String gitClone(String url, String branch, String cloneDir, String accessToken, String languageName) {


        if (url == null || url.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Git URL is required");
        }

        if (branch == null || branch.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Branch is required");
        }

        if (cloneDir == null || cloneDir.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clone directory is required");
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


    // List all files in the repository
//    public List<String> listFilesInRepository(String cloneDir, String projectName) throws IOException, GitAPIException {
//
//        // Open the cloned repository
//        Repository repository = new FileRepositoryBuilder()
//                .setGitDir(new File(cloneDir + projectName + "/.git"))
//                .build();
//
//        // Use the Git API to list all files in the repository
//        try (Git git = new Git(repository)) {
//            // Get the working tree
//            File workingDir = new File(cloneDir + projectName);
//
//            // List all files in the repository's working directory
//            return listFilesRecursively(workingDir);
//        }
//    }
//
//    // Helper method to list files recursively
//    private List<String> listFilesRecursively(File directory) {
//
//        List<String> filePaths = new ArrayList<>();
//
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    if (file.isDirectory()) {
//                        filePaths.addAll(listFilesRecursively(file)); // Recursively list files in subdirectories
//                    } else {
//                        filePaths.add(file.getName());
//                    }
//                }
//            }
//        }
//
//        return filePaths;
//    }















    // Method to list files in a repository
    public Map<String, Object> listFilesInRepository(String cloneDir, String projectName) throws IOException, GitAPIException {
        // Open the cloned repository
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(new File(cloneDir + projectName + "/.git"))
                .build();

        // Use the Git API to list all files in the repository
        try (Git git = new Git(repository)) {
            // Get the working tree
            File workingDir = new File(cloneDir + projectName);

            // List all files in the repository's working directory, organized by directory
            return buildFileTreeResponse(workingDir, "");
        }
    }

    // Helper method to list files recursively and return a hierarchical structure
    private Map<String, Object> buildFileTreeResponse(File directory, String parentPath) {
        Map<String, Object> directoryTree = new LinkedHashMap<>();
        directoryTree.put("path", parentPath);  // Current folder path

        List<String> files = new ArrayList<>();
        List<Map<String, Object>> subdirectories = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] filesInDirectory = directory.listFiles();
            if (filesInDirectory != null) {
                for (File file : filesInDirectory) {
                    if (file.isDirectory()) {
                        // Recursively add subdirectories
                        Map<String, Object> subdirectory = buildFileTreeResponse(file, parentPath + "/" + file.getName());
                        subdirectories.add(subdirectory);
                    } else {
                        // Add files in the current directory
                        String relativePath = file.getAbsolutePath().substring(directory.getAbsolutePath().length() + 1);
                        files.add(relativePath);
                    }
                }
            }
        }

        // Add files and subdirectories to the directory tree
        directoryTree.put("files", files);
        directoryTree.put("subdirectories", subdirectories);

        return directoryTree;
    }



}


