package com.matheushfp.job_position_management.modules.candidate.repositories;

import com.matheushfp.job_position_management.modules.candidate.entities.JobApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, UUID> {
}
