package com.matheushfp.job_position_management.modules.company.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "job")
@Table(name = "jobs")
@Data
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String level;
    private String description;
    private String benefits;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CompanyEntity company;

    @NotNull(message = "[companyId] needs to be specified")
    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
