package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.modules.candidate.entities.CandidateEntity;
import com.matheushfp.job_position_management.modules.candidate.repositories.CandidateRepository;
import com.matheushfp.job_position_management.modules.company.dtos.CreateJobRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
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
class JobControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private CompanyEntity company;
    private CandidateEntity candidate;

    @BeforeEach
    void createCompanyAndCandidateAndSaveInDatabase() {
        company = new CompanyEntity();
        company.setName("company_name");
        company.setUsername("company_username");
        company.setEmail("company@mail.com");
        company.setPassword("123456");
        company.setDescription("company_description");

        this.companyRepository.saveAndFlush(company);

        candidate = new CandidateEntity();
        candidate.setName("John Doe");
        candidate.setUsername("john_doe");
        candidate.setEmail("johndoe@mail.com");
        candidate.setPassword("123456");

        this.candidateRepository.saveAndFlush(candidate);
    }

    @Test
    @DisplayName("Should be able to create a new Job")
    void shouldBeAbleToCreateANewJob() throws Exception {
        CreateJobRequestDTO createJobRequest = new CreateJobRequestDTO();
        createJobRequest.setTitle("title_test");
        createJobRequest.setLevel("level_test");
        createJobRequest.setDescription("description_test");
        createJobRequest.setDescription("benefits_test");

        mvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(createJobRequest))
                        .header("Authorization", "Bearer " + TestUtils.generateCompanyToken(company.getId())))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should not be able to create a new Job if Company Not Found")
    void shouldNotBeAbleToCreateANewJobIfCompanyNotFound() throws Exception {
        CreateJobRequestDTO createJobRequest = new CreateJobRequestDTO();
        createJobRequest.setTitle("title_test");
        createJobRequest.setLevel("level_test");
        createJobRequest.setDescription("description_test");
        createJobRequest.setDescription("benefits_test");

        mvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(createJobRequest))
                        .header("Authorization", "Bearer " + TestUtils.generateCompanyToken(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should not be able to search for jobs if user doesn't have CANDIDATE role")
    void shouldNotBeAbleToSearchForJobsIfUserDoesNotHaveCandidateRole() throws Exception {
        mvc.perform(get("/jobs/search?query=title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCompanyToken(company.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should not be able to search for jobs if user doesn't provide a query")
    void shouldNotBeAbleToSearchForJobsIfQueryNotExists() throws Exception {
        mvc.perform(get("/jobs/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(candidate.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be able to search for jobs if user has CANDIDATE role")
    void shouldBeAbleToSearchForJobsIfUserIsCandidate() throws Exception {
        mvc.perform(get("/jobs/search?query=title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(candidate.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not be able to apply for job if user doesn't have CANDIDATE role")
    void shouldNotBeAbleToApplyForJobIfUserDoesNotHaveCandidateRole() throws Exception {
        UUID jobId = UUID.randomUUID();

        mvc.perform(post("/jobs/" + jobId + "/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCompanyToken(company.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should not be able to apply for job if job doesn't exists")
    void shouldNotBeAbleToApplyForJobIfJobNotFound() throws Exception {
        UUID jobId = UUID.randomUUID();

        mvc.perform(post("/jobs/" + jobId + "/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(candidate.getId())))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should not be able to apply for job if candidate not found")
    void shouldNotBeAbleToApplyForJobIfCandidateNotFound() throws Exception {
        JobEntity job = new JobEntity();
        job.setTitle("job_title");
        job.setLevel("job_level");
        job.setBenefits("job_benefits");
        job.setDescription("job_description");

        this.jobRepository.saveAndFlush(job);

        mvc.perform(post("/jobs/" + job.getId() + "/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(UUID.randomUUID())))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Should not be able to apply for job if jobId is not an UUID")
    void shouldNotBeAbleToApplyForJobIfJobIdIsNotAnUUID() throws Exception {
        mvc.perform(post("/jobs/" + "invalidJobId" + "/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(candidate.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should be able to apply for job if user has CANDIDATE role")
    void shouldBeAbleToApplyForJob() throws Exception {
        JobEntity job = new JobEntity();
        job.setTitle("job_title");
        job.setLevel("job_level");
        job.setBenefits("job_benefits");
        job.setDescription("job_description");

        this.jobRepository.saveAndFlush(job);

        mvc.perform(post("/jobs/" + job.getId() + "/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + TestUtils.generateCandidateToken(candidate.getId())))
                .andExpect(status().isCreated());
    }

}