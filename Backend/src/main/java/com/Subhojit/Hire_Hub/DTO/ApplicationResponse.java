package com.Subhojit.Hire_Hub.DTO;

import com.Subhojit.Hire_Hub.Entity.Type.StatusType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {

   private Long applicationId;

   private Long candidateId;

   private String candidateName;

   private String candidateEmail;

   private StatusType applicationStatus;
}
