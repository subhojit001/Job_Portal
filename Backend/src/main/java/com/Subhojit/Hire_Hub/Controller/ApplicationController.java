package com.Subhojit.Hire_Hub.Controller;

import com.Subhojit.Hire_Hub.DTO.ApiResponse;
import com.Subhojit.Hire_Hub.DTO.ApplicationResponse;
import com.Subhojit.Hire_Hub.DTO.StatusRequest;
import com.Subhojit.Hire_Hub.Entity.Applications;
import com.Subhojit.Hire_Hub.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/Applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;


    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse> applyJob(@PathVariable Long  jobId){
        return ResponseEntity.ok(applicationService.ApplyJob(jobId));
    }

    @GetMapping("/applicants/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<ApplicationResponse>> jobApplications(@PathVariable Long jobId){
        return ResponseEntity.ok(applicationService.jobApplicants(jobId));
    }

    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ApiResponse updateStatus(
            @PathVariable Long applicationId,
            @RequestBody StatusRequest request) {

        return applicationService.updateStatus(applicationId, request);
    }


//    @GetMapping("/applied")
//    @PreAuthorize("hasRole('JOB_SEEKER')")
//    public ResponseEntity<List<Applications>> appliedJobs(){
//        return ResponseEntity.ok(applicationService.appliedJobs());
//    }
}

