package com.matheushfp.job_position_management.modules.candidate.useCases;

import com.matheushfp.job_position_management.modules.company.entities.JobEntity;
import com.matheushfp.job_position_management.modules.company.repositories.JobRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchJobsByQueryUseCaseTest {

    @InjectMocks
    SearchJobsByQueryUseCase searchJobsByQueryUseCase;

    @Mock
    private JobRepository jobRepository;

    @Test
    @DisplayName("Should be able to Search Jobs by Query")
    void shouldBeAbleToSearchJobsByQuery() {
        JobEntity job1 = new JobEntity();
        job1.setTitle("Java Developer");

        JobEntity job2 = new JobEntity();
        job2.setTitle("Node Developer");

        when(jobRepository.findByTitleContainingIgnoreCase("java")).thenReturn(List.of(job1));

        var result = searchJobsByQueryUseCase.execute("java");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getTitle()).isEqualTo("Java Developer");
    }

}