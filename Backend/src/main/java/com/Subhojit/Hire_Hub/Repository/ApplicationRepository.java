package com.Subhojit.Hire_Hub.Repository;

import com.Subhojit.Hire_Hub.Entity.Applications;
import com.Subhojit.Hire_Hub.Entity.Job;
import com.Subhojit.Hire_Hub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Applications,Long> {

    boolean existsByUserAndJob(User user, Job job);
    List<Applications> findByJob(Job job);

    List<Applications> findByUser (User user);
}
