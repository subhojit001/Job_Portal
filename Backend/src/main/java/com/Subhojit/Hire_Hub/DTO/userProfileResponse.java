package com.Subhojit.Hire_Hub.DTO;

import com.Subhojit.Hire_Hub.Entity.Type.Roles;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class userProfileResponse {
    private Long id;

    private String name;

    private String email;

    private Roles roles;

    private String resumePath;
}
