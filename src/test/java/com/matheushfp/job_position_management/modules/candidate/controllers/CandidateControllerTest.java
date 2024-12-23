package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.utils.TestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CandidateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CandidateRepository candidateRepository;

    private CandidateEntity candidate;

    @BeforeEach
    void createCandidateEntity() {
        candidate = new CandidateEntity();
        candidate.setName("John Doe");
        candidate.setUsername("john_doe");
        candidate.setEmail("johndoe@mail.com");
        candidate.setPassword("1234567890");
    }

    @Test
    @DisplayName("Should be able to create a new Candidate")
    void shouldBeAbleToCreateANewCandidate() throws Exception {
        mvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(candidate)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should not be able to create a new Candidate if it already exists")
    void shouldNotBeAbleToCreateDuplicateCandidate() throws Exception {
        this.candidateRepository.saveAndFlush(candidate);

        mvc.perform(post("/candidates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(candidate)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should be able to Get Candidate Profile")
    void shouldBeAbleToGetCandidateProfile() throws Exception {
        this.candidateRepository.saveAndFlush(candidate);

        String candidateToken = TestUtils.generateCandidateToken(candidate.getId());

        mvc.perform(get("/candidates/me")
                .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not be able to Get Candidate Profile if candidateId is Not Found")
    void shouldNotBeAbleToGetCandidateProfileIfCandidateIdIsNotFound() throws Exception {
        String tokenWithRandomUUID = TestUtils.generateCandidateToken(UUID.randomUUID());

        mvc.perform(get("/candidates/me")
                .header("Authorization", "Bearer " + tokenWithRandomUUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not be able to Get Candidate Profile if User is Unauthorized")
    void shouldNotBeAbleToGetCandidateProfileIfUserIsUnauthorized() throws Exception {
        mvc.perform(get("/candidates/me")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should not be able to Get Candidate Profile if token doesn't have CANDIDATE role")
    void shouldNotBeAbleToGetCandidateProfileIfTokenNotContainsCandidateRole() throws Exception {
        String companyToken = TestUtils.generateCompanyToken(UUID.randomUUID());

        mvc.perform(get("/candidates/me")
                        .header("Authorization", "Bearer " + companyToken))
                .andExpect(status().isForbidden());
    }

}