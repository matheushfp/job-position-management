package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.CandidateAlreadyExistsException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        this.candidateRepository.findByUsernameOrEmail(candidateEntity.getUsername(), candidateEntity.getEmail())
                .ifPresent((candidate) -> {
                    throw new CandidateAlreadyExistsException();
                });

        String passwordHash = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(passwordHash);

        return this.candidateRepository.save(candidateEntity);
    }
}
