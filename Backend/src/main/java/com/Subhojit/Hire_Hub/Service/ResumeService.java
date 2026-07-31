package com.Subhojit.Hire_Hub.Service;

import com.Subhojit.Hire_Hub.DTO.ResumeUploadResponse;
import com.Subhojit.Hire_Hub.Entity.User;
import com.Subhojit.Hire_Hub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ResumeUploadResponse uploadResume(MultipartFile file){

        if(file.isEmpty()){
            throw new RuntimeException("File is empty");
        }

        if(!file.getOriginalFilename().toLowerCase().endsWith(".pdf")){
            throw new RuntimeException("file type is incorrect");
        }

        User user =userService.getCurrentUser();

        try{

            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String fileName= UUID.randomUUID()+"_"+ file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

            user.setResumePath(filePath.toString());

            userRepository.save(user);

            return ResumeUploadResponse.builder()
                    .fileName(fileName)
                    .message("Resume uploaded successfully")
                    .resumePath(user.getResumePath())
                    .build();


        } catch (IOException e) {
            throw new RuntimeException("upload failed",e);
        }
    }

    public ResponseEntity<Resource> downloadResume(Long userId){
        User user = userRepository.findById(userId).orElseThrow();

        if(user.getResumePath()==null || user.getResumePath().isEmpty()){
            throw new RuntimeException("Resume not uploaded");
        }

        try {
            Path filePath = Paths.get(user.getResumePath());

            if(!Files.exists(filePath)){
                throw new RuntimeException("Resume file not found");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if(!resource.exists() || !resource.isReadable()){
                throw new RuntimeException("");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filePath.getFileName().toString() + "\""
                    )
                    .body(resource);

        }catch (MalformedURLException e){
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<Resource> viewResume(Long userId) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getResumePath() == null) {
            throw new RuntimeException("Resume not uploaded");
        }

        Path path = Paths.get(user.getResumePath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("Resume file not found");
        }

        String fileName = path.getFileName().toString();

        String contentType = Files.probeContentType(path);

        if (contentType == null) {
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
