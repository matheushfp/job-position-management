package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchJobsByQueryUseCase {

    @Autowired
    private JobRepository jobRepository;

    public List<JobEntity> execute(String query) {
        return this.jobRepository.findByTitleContainingIgnoreCase(query);
    }
}
