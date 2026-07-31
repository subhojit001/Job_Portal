package com.Subhojit.Hire_Hub.DTO;

import com.Subhojit.Hire_Hub.Entity.Type.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusRequest {
        private StatusType status;
}
