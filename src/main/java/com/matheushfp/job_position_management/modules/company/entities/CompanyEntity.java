package com.matheushfp.job_position_management.modules.company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "company")
@Table(name = "companies")
@Data
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(regexp = "\\S+", message = "[username] must not contain spaces.")
    private String username;

    @Email(message = "[email] should be a valid e-mail.")
    private String email;

    @Length(min = 4, message = "[password] must contain at least 4 characters.")
    private String password;

    private String name;
    private String description;
    private String website;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
