package com.Subhojit.Hire_Hub.Entity;

import com.Subhojit.Hire_Hub.Entity.Type.StatusType;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false )
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    private StatusType status;
}
