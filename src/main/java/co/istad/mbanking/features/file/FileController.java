package co.istad.mbanking.features.file;

import co.istad.mbanking.exception.ApiResponse;
import co.istad.mbanking.features.file.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    // Single file upload
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponse>> upload(@RequestPart MultipartFile file) throws IOException {
        FileResponse fileResponse = fileService.upload(file);

        // Building the API response
        ApiResponse<FileResponse> apiResponse = ApiResponse.<FileResponse>builder()
                .success(true)
                .message("File uploaded successfully")
                .status(HttpStatus.CREATED)
                .payload(fileResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Multiple files upload
    @PostMapping(value = "/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileResponse>>> uploadMultiple(@RequestPart List<MultipartFile> files) throws IOException {
        List<FileResponse> fileResponses = fileService.uploadMultiple(files);

        // Building the API response
        ApiResponse<List<FileResponse>> apiResponse = ApiResponse.<List<FileResponse>>builder()
                .success(true)
                .message("Files uploaded successfully")
                .status(HttpStatus.CREATED)
                .payload(fileResponses)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Preview image by filename
    @GetMapping("/preview/{filename}")
    public ResponseEntity<?> previewImage(@PathVariable String filename) throws IOException {
        return fileService.previewImage(filename);
    }

}
