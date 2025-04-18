package com.ecommerce.demo.service;

import com.ecommerce.demo.config.StorageProperties;
import com.ecommerce.demo.exceptions.StorageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStoreService {
    private StorageProperties properties = new StorageProperties();
    Path rootLocation = Paths.get(properties.getLocation());

    // Diese Methode speichert die hochgeladene Datei in upload-dir und gibt die URL der gespeicherten Datei zurück.
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadedFileName = UUID.randomUUID().toString() + "." + extension;

            // C:\\Users\\DeinBenutzer\\workspace\\upload-dir\\3f2b4c1a-9e47-4c5a-b345-1234567890ab.jpg
            Path destinationFile = rootLocation.resolve(Paths.get(uploadedFileName))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

                final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

                return baseUrl + "/fileUpload/files" + uploadedFileName; // z.B: http://localhost:8080/fileUpload/files/3f2b4c1a-9e47-4c5a-b345-1234567890ab.jpg
            }
        }
        catch(IOException e) {
                throw new StorageException("Failed to store file. ", e);
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Resource load(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }


}
