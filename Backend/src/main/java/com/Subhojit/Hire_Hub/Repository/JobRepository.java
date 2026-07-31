package com.Subhojit.Hire_Hub.Repository;

import com.Subhojit.Hire_Hub.DTO.JobResponse;
import com.Subhojit.Hire_Hub.Entity.Job;
import com.Subhojit.Hire_Hub.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobRepository extends JpaRepository<Job,Long>, JpaSpecificationExecutor<Job> {
    //List<JobResponse> findAllJobs();
    List<Job> findByCreatedBy(User user);

    Page<Job> findAll(Specification<Job> spec, Pageable pageable);
}
