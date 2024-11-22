package co.istad.inspectra.features.file;


import co.istad.inspectra.features.file.dto.FileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * this interface is used to define the methods that will be implemented in the FileServiceImpl class
 * @see FileServiceImpl
 * Author: Phiv Lyhou
 */
public interface FileService {
    /**
     * this method is used to upload a single file
     * @param file the file to be uploaded
     * @param request the request object
     * @return{@link FileResponse} the response object
     */
    FileResponse uploadSingleFile(MultipartFile file, HttpServletRequest request);

    /**
     * this method is used to upload multiple files
     * @param files the files to be uploaded
     * @param request the request object
     * @return{@link List<String>} the response object
     */
    List<String> uploadMultipleFiles(MultipartFile[] files,HttpServletRequest request);

    /**
     * this method is used to serve a file
     * @param filename the name of the file to be served
     * @param request the request object
     * @return{@link ResponseEntity<Resource>} the response object
     */
    ResponseEntity<Resource> serveFile(String filename, HttpServletRequest request);

    /**
     * this method is used to delete a file
     * @param filename the name of the file to be deleted
     */
    void deleteFile(String filename);
}
