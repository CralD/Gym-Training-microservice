package com.epam.trainer_service.service;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTrainingToTrainer() {
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto(
                "Enrique.Rodriguez",
                "Enrique",
                "Rodriguez",
                true,
                "2024-09-15T00:00:00.000+00:00",
                45.0,
                "add"
        );


        Trainer trainer = new Trainer();
        trainer.setUserName(workloadDto.getUserName());
        trainer.setFirstName(workloadDto.getFirstName());
        trainer.setLastName(workloadDto.getLastName());
        trainer.setActive(workloadDto.isStatus());
        trainer.setTrainingSummary(new HashMap<>());
        trainer.getTrainingSummary().put(workloadDto.getTrainingDate(), workloadDto.getTrainingDuration());

        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Optional<Trainer> result = trainerService.saveOrUpdateUserTraining(workloadDto);

        assertNotNull(result);
        assertTrue(result.isPresent()); // Check if result contains a value
        assertEquals(workloadDto.getUserName(), result.get().getUserName()); // Use result.get() to access the Trainer object
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    public void deleteTrainingToTrainer() {
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto(
                "Juan.Perez", "Juan", "Perez",
                true, "2024-09-15T00:00:00.000+00:00", 2.0,
                "DELETE");

        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("Juan.Perez");

        when(trainerRepository.findByUserName("Juan.Perez")).thenReturn(trainer);
        doNothing().when(trainerRepository).deleteById(1L);

        // Call the service method
        Optional<Trainer> result = trainerService.saveOrUpdateUserTraining(workloadDto);

        // Verify interactions and assert results
        verify(trainerRepository).findByUserName("Juan.Perez");
        verify(trainerRepository).deleteById(1L);
        assert (result.isEmpty());
    }

    @Test
    public void getTrainerMonthlyHours() {
        String username = "Juan.Perez";
        String yearMonth = "2024-09-15T00:00:00.000+00:00";
        double expectedHours = 15.0;

        Trainer trainer = new Trainer();
        Map<String, Double> trainingDetails = new HashMap<>();
        trainingDetails.put(yearMonth, expectedHours);
        trainer.setTrainingSummary(trainingDetails);

        when(trainerRepository.findByUserName(username)).thenReturn(trainer);

        // Execution
        Optional<Double> actualHours = trainerService.getTotalHoursForMonth(username, yearMonth);

        // Assertion
        assertEquals(expectedHours, actualHours);

    }

    @Test
    void whenTrainerExistsAndNoHoursForMonth_returnsZero() {
        // Setup
        String username = "john_doe";
        String yearMonth = "2022-01";

        Trainer trainer = new Trainer();
        Map<String, Double> trainingDetails = new HashMap<>();
        trainer.setTrainingSummary(trainingDetails);  // No entry for the yearMonth

        when(trainerRepository.findByUserName(username)).thenReturn(trainer);

        // Execution
        Optional<Double> actualHours = trainerService.getTotalHoursForMonth(username, yearMonth);

        // Assertion
        assertEquals(0.0, actualHours);
    }

    @Test
    void whenTrainerDoesNotExist_returnsZero() {
        // Setup
        String username = "john_doe";
        String yearMonth = "2022-01";

        when(trainerRepository.findByUserName(username)).thenReturn(null);

        // Execution
        Optional<Double> actualHours = trainerService.getTotalHoursForMonth(username, yearMonth);

        // Assertion
        assertEquals(0.0, actualHours);
    }


}
