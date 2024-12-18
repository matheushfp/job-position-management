package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.modules.company.dtos.CreateJobRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import com.matheushfp.job_position_management.modules.utils.TestUtils;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JobControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Should be able to create a new Job")
    void shouldBeAbleToCreateANewJob() throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName("COMPANY_NAME");
        companyEntity.setUsername("COMPANY_USERNAME");
        companyEntity.setEmail("company@mail.com");
        companyEntity.setPassword("123456");
        companyEntity.setDescription("COMPANY_DESCRIPTION");

        var company = this.companyRepository.saveAndFlush(companyEntity);

        CreateJobRequestDTO createJobRequest = new CreateJobRequestDTO();
        createJobRequest.setTitle("TITLE_TEST");
        createJobRequest.setLevel("LEVEL_TEST");
        createJobRequest.setDescription("DESCRIPTION_TEST");
        createJobRequest.setDescription("BENEFITS_TEST");

        mvc.perform(post("/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.objectToJson(createJobRequest))
                        .header("Authorization", "Bearer " + TestUtils.generateToken(company.getId())))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should not be able to create a new Job if Company Not Found")
    void shouldNotBeAbleToCreateANewJobIfCompanyNotFound() throws Exception {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName("COMPANY_NAME");
        companyEntity.setUsername("COMPANY_USERNAME");
        companyEntity.setEmail("company@mail.com");
        companyEntity.setPassword("123456");
        companyEntity.setDescription("COMPANY_DESCRIPTION");

        var company = this.companyRepository.saveAndFlush(companyEntity);

        CreateJobRequestDTO createJobRequest = new CreateJobRequestDTO();
        createJobRequest.setTitle("TITLE_TEST");
        createJobRequest.setLevel("LEVEL_TEST");
        createJobRequest.setDescription("DESCRIPTION_TEST");
        createJobRequest.setDescription("BENEFITS_TEST");

        mvc.perform(post("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(createJobRequest))
                        .header("Authorization", "Bearer " + TestUtils.generateToken(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }
}