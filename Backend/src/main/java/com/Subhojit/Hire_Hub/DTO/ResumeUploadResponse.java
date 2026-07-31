package com.Subhojit.Hire_Hub.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeUploadResponse {
    private String message;

    private String fileName;

    private String resumePath;
}
