package com.matheushfp.job_position_management.modules.company.repositories;

import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
}