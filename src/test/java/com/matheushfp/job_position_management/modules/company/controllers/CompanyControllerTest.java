package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companyRepository;

    private CompanyEntity company;

    @BeforeEach
    void createCompanyEntity() {
        company = new CompanyEntity();
        company.setName("Company");
        company.setUsername("company");
        company.setEmail("company@mail.com");
        company.setPassword("1234567");
    }

    @Test
    @DisplayName("Should be able to create a new Company")
    void shouldBeAbleToCreateANewCompany() throws Exception {
        mvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(company)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should not be able to create a new Company if it already exists")
    void shouldNotBeAbleToCreateDuplicateCompany() throws Exception {
        this.companyRepository.saveAndFlush(company);

        mvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(company)))
                .andExpect(status().isConflict());
    }
}