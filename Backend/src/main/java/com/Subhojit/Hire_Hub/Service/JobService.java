package com.Subhojit.Hire_Hub.Service;

import com.Subhojit.Hire_Hub.DTO.JobCreateRequest;
import com.Subhojit.Hire_Hub.DTO.JobResponse;
import com.Subhojit.Hire_Hub.Entity.Job;
import com.Subhojit.Hire_Hub.Entity.User;
import com.Subhojit.Hire_Hub.Repository.JobRepository;
import com.Subhojit.Hire_Hub.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final UserService userService;

    public JobResponse createJob(JobCreateRequest request){

        User user = userService.getCurrentUser();

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .company(request.getCompany())
                .location(request.getLocation())
                .salary(request.getSalary())
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .build();
        Job savedJob= jobRepository.save(job);

        return JobResponse.builder()
                .id(savedJob.getId())
                .title(savedJob.getTitle())
                .description(savedJob.getDescription())
                .company(savedJob.getCompany())
                .location(savedJob.getLocation())
                .salary(savedJob.getSalary())
                .postedAt(savedJob.getPostedAt())
                .build();
    }

    public Page<JobResponse> getAllJobs(Pageable pageable, String title, String location,String company){
        Specification<Job> specification = (root,query,cb)-> cb.conjunction();

        if(title != null && !title.isBlank()){
            specification = specification.and((root,query,cb) ->
                                             cb.like(root.get("title"),"%"+title.toLowerCase()+"%"));
        }

        if(location != null && !location.isBlank()){
            specification = specification.and((root,query,cb) ->
                    cb.like(cb.lower(root.get("location")),"%"+location.toLowerCase()+"%"));
        }

        if(company != null && !company.isBlank()){
            specification = specification.and((root,query,cb) ->
                    cb.like(root.get("company"),"%"+company.toLowerCase()+"%"));
        }

        Page<Job> jobs=jobRepository.findAll(specification,pageable);

        return jobs.map(job -> JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .salary(job.getSalary())
                .location(job.getLocation())
                .postedAt(job.getPostedAt())
                .recruiterName(job.getCreatedBy().getName())
                .recruiterEmail(job.getCreatedBy().getEmail())
                .build());
    }

    public List<JobResponse> myJobs(){

        User user = userService.getCurrentUser();

        List<Job> jobs = jobRepository.findByCreatedBy(user);

        return jobs.stream().map(job -> JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .location(job.getLocation())
                .salary(job.getSalary())
                .postedAt(job.getPostedAt())
                .build()).toList();
    }
}
