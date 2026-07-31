package com.Subhojit.Hire_Hub.Repository;

import com.Subhojit.Hire_Hub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>  findByemail(String email);
}
