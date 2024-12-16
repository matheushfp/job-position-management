package com.matheushfp.job_position_management.modules.candidate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "JobApplication")
@Table(name = "job_applications")
@Data
public class JobApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CandidateEntity candidate;

    @Column(name = "candidate_id")
    private UUID candidateId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "job_id", referencedColumnName = "id", insertable = false, updatable = false)
    private JobEntity job;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public JobApplicationEntity(UUID candidateId, UUID jobId) {
        this.candidateId = candidateId;
        this.jobId = jobId;
    }
}
