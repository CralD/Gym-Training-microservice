package com.epam.trainer_service.controller;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.service.TrainerService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainerController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
})
class TrainerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    @Test
    void whenRegisterTrainerTrainingWithValidData_thenReturnsTrainer() throws Exception {
        // Assuming TrainerWorkloadDto is immutable and requires all fields in constructor
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto(
                "Juan.Perez", // userName
                "Juan",       // firstName (assuming needed for "add")
                "Perez",      // lastName (assuming needed for "add")
                true,         // status (assuming needed for "add")
                "2024-09-15T00:00:00.000+00:00", // trainingDate (assuming needed for "add")
                45.0,         // trainingDuration (assuming needed for "add")
                "add"         // actionType
        );

        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("Juan.Perez");

        // Mocking the service to return Optional of trainer
        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(Optional.of(trainer));

        // Perform the request and expect 200 OK status
        mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"Juan.Perez\",\"actionType\":\"add\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Juan.Perez"));
    }

    @Test
    void whenRegisterTrainerTrainingForDeletionAndTrainerNotFound_thenReturnsNoContent() throws Exception {
        // Create dto using constructor if TrainerWorkloadDto is immutable
        TrainerWorkloadDto dto = new TrainerWorkloadDto(
                "JohnDoe", // userName
                null,      // firstName (not needed for deletion)
                null,      // lastName (not needed for deletion)
                false,     // status (not needed for deletion)
                null,      // trainingDate (not needed for deletion)
                0.0,       // trainingDuration (not needed for deletion)
                "delete"   // actionType
        );

        // Mocking the service to return Optional.empty() to simulate trainer not found
        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(Optional.empty());

        // Perform the request and expect 204 No Content status
        mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"JohnDoe\",\"actionType\":\"delete\"}"))
                .andExpect(status().isNoContent());
    }
}