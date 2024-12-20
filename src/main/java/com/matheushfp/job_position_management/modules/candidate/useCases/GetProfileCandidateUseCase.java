package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.exceptions.UserNotFoundException;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.candidate.dtos.GetProfileCandidateResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetProfileCandidateUseCase {

    @Autowired
    private CandidateRepository candidateRepository;

    public GetProfileCandidateResponseDTO execute(UUID candidateId) {
        CandidateEntity candidate = this.candidateRepository.findById(candidateId)
                .orElseThrow(() -> new UserNotFoundException("Candidate Not Found"));

        var candidateResponseDTO = GetProfileCandidateResponseDTO.builder()
                .id(candidate.getId())
                .username(candidate.getUsername())
                .name(candidate.getName())
                .email(candidate.getEmail())
                .description(candidate.getDescription())
                .build();

        return candidateResponseDTO;
    }
}
