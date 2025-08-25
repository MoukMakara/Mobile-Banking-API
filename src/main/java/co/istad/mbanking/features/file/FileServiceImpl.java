package co.istad.mbanking.features.file;

import co.istad.mbanking.features.file.dto.FileResponse;
import co.istad.mbanking.util.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${file-server.server-path}")
    private String serverPath;

    @Value("${file-server.base-uri}")
    private String baseUri;


    @Override
    public List<FileResponse> uploadMultiple(List<MultipartFile> files) throws IOException {

        List<FileResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            responses.add(upload(file));
        }

        return responses;
    }


    @Override
    public FileResponse upload(MultipartFile file) throws IOException {

        String newName = FileUtil.generateFileName(file.getOriginalFilename());
        String extension = FileUtil.extractExtension(file.getOriginalFilename());

        Path path = Path.of(serverPath + newName);
        Files.copy(file.getInputStream(), path);

        return FileResponse.builder()
                .name(newName)
                .size(file.getSize())
                .extension(extension)
                .contentType(file.getContentType())
                .uri(baseUri + newName)
                .build();
    }

    @Override
    public ResponseEntity<?> previewImage(String filename) throws IOException {
        try {
            // Create path to the file
            Path filePath = Paths.get(serverPath + filename);

            // Check if file exists
            if (!Files.exists(filePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found: " + filename);
            }

            // Load file as resource
            Resource resource = new UrlResource(filePath.toUri());

            // Determine content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default to binary if type cannot be determined
            }

            // Return the resource with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading image: " + filename);
        }
    }
}
