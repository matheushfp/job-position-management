package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.JobNotFoundException;
import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.candidate.entities.JobApplicationEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.candidate.repositories.JobApplicationRepository;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplyForJobUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public JobApplicationEntity execute(UUID candidateId, UUID jobId) {
        this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new UserNotFoundException());

        this.jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException());

        var jobApplication = new JobApplicationEntity(candidateId, jobId);

        return this.jobApplicationRepository.save(jobApplication);
    }
}
