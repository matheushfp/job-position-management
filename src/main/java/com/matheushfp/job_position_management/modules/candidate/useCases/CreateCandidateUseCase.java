package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.modules.candidate.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
                .ifPresent((candidate) -> {
                    throw new CandidateAlreadyExistsException();
                });

        return this.candidateRepository.save(candidateEntity);
    }
}
