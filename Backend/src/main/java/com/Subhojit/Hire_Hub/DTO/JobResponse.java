package com.Subhojit.Hire_Hub.DTO;

import com.Subhojit.Hire_Hub.Entity.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobResponse {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String company;

    private String location;

    private Double salary;

    private LocalDateTime postedAt;

    private String recruiterName;

    private String recruiterEmail;
}
