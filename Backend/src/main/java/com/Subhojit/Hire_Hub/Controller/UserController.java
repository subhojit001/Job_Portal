package com.Subhojit.Hire_Hub.Controller;

import com.Subhojit.Hire_Hub.DTO.*;
import com.Subhojit.Hire_Hub.Service.ResumeService;
import com.Subhojit.Hire_Hub.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ResumeService resumeService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/upload-resume")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(resumeService.uploadResume(file));
    }

    @GetMapping("/download-resume/{userId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long userId){
        return resumeService.downloadResume(userId);
    }

    @GetMapping("/my-profile")
    public ResponseEntity<userProfileResponse> getProfile(){
        return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<userProfileResponse> updateProfile(@RequestBody @Valid ProfileUpdateRequest request){
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @GetMapping("/view-resume/{userId}")
    public ResponseEntity<Resource> viewResume(@PathVariable Long userId) throws IOException {

        return resumeService.viewResume(userId);

    }
}
