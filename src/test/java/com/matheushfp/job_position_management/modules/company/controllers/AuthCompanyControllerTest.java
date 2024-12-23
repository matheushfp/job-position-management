package com.matheushfp.job_position_management.modules.company.controllers;

import com.matheushfp.job_position_management.dtos.AuthRequestDTO;
import com.matheushfp.job_position_management.modules.company.entities.CompanyEntity;
import com.matheushfp.job_position_management.modules.company.repositories.CompanyRepository;
import com.matheushfp.job_position_management.modules.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class AuthCompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void createCompanyAndStoreInDatabase() {
        CompanyEntity company = new CompanyEntity();
        company.setName("Company");
        company.setUsername("company");
        company.setEmail("company@mail.com");
        company.setPassword(new BCryptPasswordEncoder().encode("1234567"));

        this.companyRepository.saveAndFlush(company);
    }

    @AfterEach
    void cleanDatabase() {
        this.companyRepository.deleteAll();
    }

    @Test
    @DisplayName("Should be able to Authenticate Company")
    void shouldBeAbleToAuthenticateCompany() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("company", "1234567");

        mvc.perform(post("/auth/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not be able to Authenticate Company With Wrong Username")
    void shouldNotBeAbleToAuthenticateCompanyWithWrongUsername() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("wrong_username", "1234567");

        mvc.perform(post("/auth/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should not be able to Authenticate Company With Wrong Password")
    void shouldNotBeAbleToAuthenticateCompanyWithWrongPassword() throws Exception {
        AuthRequestDTO requestBody = new AuthRequestDTO("company", "wrong_password");

        mvc.perform(post("/auth/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJson(requestBody)))
                .andExpect(status().isUnauthorized());
    }

}