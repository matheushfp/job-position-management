package com.matheushfp.job_position_management.modules.candidate.controllers;

import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.utils.TestUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthCandidateControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CandidateRepository candidateRepository;

    @BeforeEach
    void createCandidateAndStoreInDatabase() {
        CandidateEntity candidate = new CandidateEntity();
        candidate.setName("John Doe");
        candidate.setUsername("john_doe");
        candidate.setEmail("johndoe@mail.com");
        candidate.setPassword(new BCryptPasswordEncoder().encode("12345678"));

        this.candidateRepository.saveAndFlush(candidate);
    }

    @AfterEach
    void cleanDatabase() {
        this.candidateRepository.deleteAll();
    }

    @Test
    @DisplayName("Should be able to Authenticate Candidate")
    void shouldBeAbleToAuthenticateCandidate() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("john_doe", "12345678");

        mvc.perform(post("/auth/candidate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not be able to Authenticate Candidate With Wrong Username")
    void shouldNotBeAbleToAuthenticateCandidateWithWrongUsername() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("wrong_username", "12345678");

        mvc.perform(post("/auth/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not be able to Authenticate Candidate With Wrong Password")
    void shouldNotBeAbleToAuthenticateCandidateWithWrongPassword() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("john_doe", "wrong_password");

        mvc.perform(post("/auth/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isUnauthorized());
    }
}