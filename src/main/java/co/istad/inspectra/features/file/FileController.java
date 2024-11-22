package co.istad.inspectra.features.file;


import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping(value = "", consumes = "multipart/form-data")
    @Operation(summary = "Upload a single file")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<FileResponse> uploadSingleFile(@Valid
            @RequestPart("file") MultipartFile file, HttpServletRequest request
    ) {
        return BaseRestResponse
                .<FileResponse>builder()
                .data(fileService.uploadSingleFile(file,request))
                .status(HttpStatus.CREATED.value())
                .message("File uploaded successfully")
                .build();
    }


    @PostMapping(value = "/multiple", consumes = "multipart/form-data")
    @Operation(summary = "Upload multiple files")
    public BaseRestResponse<List<String>> uploadMultipleFiles(@RequestPart("files") MultipartFile[] files, HttpServletRequest request) {

        return BaseRestResponse
                .<List<String>>builder()
                .data(fileService.uploadMultipleFiles(files,request))
                .status(HttpStatus.CREATED.value())
                .message("Files uploaded successfully")
                .build();

    }


    @GetMapping("/download/{fileName}")
    @Operation(summary = "Download a file")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName, HttpServletRequest request){

        return fileService.serveFile(fileName,request);

    }


    @DeleteMapping("{fileName}")
    @Operation(summary = "Delete a file")
    public String deleteFile(@PathVariable String fileName) {

        fileService.deleteFile(fileName);

        return "file is deleted successfully!";
    }


}
