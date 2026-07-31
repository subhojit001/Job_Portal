package com.Subhojit.Hire_Hub.Service;

import com.Subhojit.Hire_Hub.DTO.ApiResponse;
import com.Subhojit.Hire_Hub.DTO.ApplicationResponse;
import com.Subhojit.Hire_Hub.DTO.JobResponse;
import com.Subhojit.Hire_Hub.DTO.StatusRequest;
import com.Subhojit.Hire_Hub.Entity.Applications;
import com.Subhojit.Hire_Hub.Entity.Job;
import com.Subhojit.Hire_Hub.Entity.Type.StatusType;
import com.Subhojit.Hire_Hub.Entity.User;
import com.Subhojit.Hire_Hub.Repository.ApplicationRepository;
import com.Subhojit.Hire_Hub.Repository.JobRepository;
import com.Subhojit.Hire_Hub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final UserService userService;


    public ApiResponse ApplyJob(Long jobId){
        User user = userService.getCurrentUser();

        Job job = jobRepository.findById(jobId).orElseThrow();

        if(applicationRepository.existsByUserAndJob(user,job)){
            throw new RuntimeException("Already applied");
        }

        Applications applications = Applications.builder()
                .user(user)
                .job(job)
                .status(StatusType.APPLIED)
                .build();
        applicationRepository.save(applications);

        return new ApiResponse("Applied successfully");

    }

//    public List<Applications> jobApplications(Long jobId){
//          return applicationRepository.findByJob(job);
//    }

    public List<ApplicationResponse> jobApplicants(Long jobId){
        User recruiter = userService.getCurrentUser();

        Job job = jobRepository.findById(jobId).orElseThrow();

        if(!job.getCreatedBy().getId().equals(recruiter.getId())){
            throw new RuntimeException("you are not allowed to access this!!");
        }

        List<Applications> applications = applicationRepository.findByJob(job);

        return applications.stream()
                .map(application -> ApplicationResponse.builder()
                        .applicationId(application.getId())
                        .candidateId(application.getUser().getId())
                        .candidateName(application.getUser().getName())
                        .candidateEmail(application.getUser().getEmail())
                        .applicationStatus(application.getStatus())
                        .build())
                .toList();
    }

    public ApiResponse updateStatus(Long id, StatusRequest request){
        User recruiter = userService.getCurrentUser();

        Applications applications = applicationRepository.findById(id).orElseThrow();

        Job job = applications.getJob();
        if(!job.getCreatedBy().getId().equals(recruiter.getId())){
            throw new RuntimeException("You are not allowed");
        }

        if(applications.getStatus()!= StatusType.APPLIED){
            throw new RuntimeException("Already changed");
        }

        applications.setStatus(request.getStatus());
        applicationRepository.save(applications);

        return new ApiResponse("Status updated");
    }
}
