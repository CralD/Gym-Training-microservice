package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TrainingRepository;

import com.epam.gymappHibernate.entity.Training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb7")
class TrainingServiceTest {


    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training();
        training.setTrainingId(1L);
        training.setTrainingDate(new Date());
    }

    @Test
    public void testSaveTraining() {
        doNothing().when(trainingRepository).saveTraining(any(Training.class));
        trainingService.saveTraining(training);
        verify(trainingRepository, times(1)).saveTraining(training);
    }


    @Test
    public void testGetTraineeTrainings() {
        String username = "johndoe";
        Date fromDate = new Date();
        Date toDate = new Date();
        String trainerName = "trainer";
        String trainingType = "type";

        when(trainingRepository.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(Collections.singletonList(training));

        List<Training> result = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training, result.get(0));
    }

    @Test
    public void testGetTrainerTrainings() {
        String username = "trainer";
        Date fromDate = new Date();
        Date toDate = new Date();
        String traineeName = "johndoe";

        when(trainingRepository.getTrainerTrainings(username, fromDate, toDate, traineeName))
                .thenReturn(Collections.singletonList(training));

        List<Training> result = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training, result.get(0));
    }
}