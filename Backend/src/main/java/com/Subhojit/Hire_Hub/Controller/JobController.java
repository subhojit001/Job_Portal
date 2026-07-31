package com.Subhojit.Hire_Hub.Controller;

import com.Subhojit.Hire_Hub.DTO.JobCreateRequest;
import com.Subhojit.Hire_Hub.DTO.JobResponse;
import com.Subhojit.Hire_Hub.Entity.Job;
import com.Subhojit.Hire_Hub.Service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobCreateRequest jobCreateRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(jobCreateRequest));
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> showAllJobs(@RequestParam(required = false , defaultValue = "1") int page,
                                                 @RequestParam(required = false , defaultValue = "5") int size,
                                                 @RequestParam(required = false) String sortBy,
                                                 @RequestParam(required = false) String direction,
                                                 @RequestParam(required = false) String title,
                                                 @RequestParam(required = false) String company,
                                                 @RequestParam(required = false) String location){

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(page-1,size,sort);
        return ResponseEntity.ok(jobService.getAllJobs(pageable,title,location,company));
    }

    @GetMapping("/myjobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobResponse>> showMyJobs(){
        return ResponseEntity.ok(jobService.myJobs());
    }


}
