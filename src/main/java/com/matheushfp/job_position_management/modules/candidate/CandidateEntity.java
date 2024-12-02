package com.matheushfp.job_position_management.modules.candidate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "candidate")
@Table(name = "candidates")
public class CandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Pattern(regexp = "\\S+", message = "[username] must not contain spaces.")
    private String username;

    private String description;

    @Email(message = "[email] should be a valid e-mail.")
    private String email;

    @Length(min = 4, message = "[password] must contain at least 4 characters.")
    private String password;

    private String curriculum;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
