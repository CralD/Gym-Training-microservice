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

        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto(
                "Juan.Perez",
                "Juan",
                "Perez",
                true,
                "2024-09-15T00:00:00.000+00:00",
                45.0,
                "add"
        );

        Trainer trainer = new Trainer();
        trainer.setTrainerId("1L");
        trainer.setUserName("Juan.Perez");


        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(Optional.of(trainer));


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
                "JohnDoe",
                null,
                null,
                false,
                null,
                0.0,
                "delete"
        );


        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"JohnDoe\",\"actionType\":\"delete\"}"))
                .andExpect(status().isNoContent());
    }
}