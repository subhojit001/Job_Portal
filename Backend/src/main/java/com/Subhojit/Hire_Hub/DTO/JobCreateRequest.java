package com.Subhojit.Hire_Hub.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String company;

    private String location;

    private Double salary;
}
