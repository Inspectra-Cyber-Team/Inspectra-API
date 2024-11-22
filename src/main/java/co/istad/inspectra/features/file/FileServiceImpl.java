package co.istad.inspectra.features.file;


import co.istad.inspectra.features.file.dto.FileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {

    // this annotation is used to inject the value of the property file_storage.image_location
    @Value("${file_storage.image_location}")
    String fileStorageDir;

    // this set is used to store the supported image types
    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE);


    // this method is used to generate the image url
    private String generateImageUrl(HttpServletRequest request, String filename) {

        return String.format("%s://%s:%d/images/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                filename);
    }

    // this method is used to generate the download image url
    private String generateDownloadImageUrl(HttpServletRequest request, String filename) {

        return String.format("%s://%s:%d/api/v1/files/download/%s",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort(),
                filename);
    }

    // this method is used to upload a file
    private String uploadFile(MultipartFile file) {

        String contentType = file.getContentType();

        if (!SUPPORTED_IMAGE_TYPES.contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    contentType + " not  allowed!! ");

        }
        try {
//       Check if the directory doesn't exist , we will create the directory
            Path fileStoragePath = Path.of(fileStorageDir);

            if (!Files.exists(fileStoragePath)) {
                Files.createDirectories(fileStoragePath);
            }

            String fileName = UUID.randomUUID() + "." +
                    Objects.requireNonNull(file.getOriginalFilename())
                            .split("\\.")[1];
            // handle if there are more than one dot !

            Files.copy(file.getInputStream(),
                    fileStoragePath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to upload file !!");

        }

    }

    // this method is used to upload a single file
    @Override
    public FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request) {

        String filename = uploadFile(file);

        String fullImageUrl = generateImageUrl(request, filename);

        return FileResponse.builder()
                .downloadUrl(generateDownloadImageUrl(request, filename))
                .fileType(file.getContentType())
                .size((float) file.getSize() / 1024) // in KB
                .filename(filename)
                .fullUrl(fullImageUrl).build();

    }


    // this method is used to upload multiple files
    @Override
    public List<String> uploadMultipleFiles(MultipartFile[] files, HttpServletRequest request) {

        var fileUrls = new ArrayList<String>();

        for (var file : files) {
            FileResponse fileResponse = uploadSingleFile(file, request);
            fileUrls.add(fileResponse.fullUrl());
        }

        return fileUrls;
    }


    // this method is used to serve a file
    @Override
    public ResponseEntity<Resource> serveFile(String filename, HttpServletRequest request) {
        try {
            //        get path of the images
            Path imagePath = Path.of(fileStorageDir).resolve(filename);

            Resource resourceUrl = new UrlResource(imagePath.toUri());

            if (resourceUrl.exists()) {

                return ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType("image/jpeg"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceUrl.getFilename() + "\"")
                        .body(resourceUrl);

            } else {
                // bad request
                throw new RuntimeException("Resources not found ! ");
            }

        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "File not found !!");
        }

    }


    // this method is used to delete a file
    @Override
    public void deleteFile(String filename) {

        try {
            Path imagePath = Path.of(fileStorageDir).resolve(filename);
            Files.deleteIfExists(imagePath);

        } catch (IOException ex) {
           throw new ResponseStatusException(
                   HttpStatus.NOT_FOUND,
                   "File not found !!");
        }

    }
}
