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
        TrainerWorkloadDto workloadDto = new TrainerWorkloadDto();
        workloadDto.setUserName("Juan.Perez");
        workloadDto.setActionType("DELETE");

        workloadDto.setFirstName("Juan");
        workloadDto.setLastName("Perez");
        workloadDto.setTrainingDate("2021-01-01");
        workloadDto.setTrainingDuration(2.0);
        workloadDto.setStatus(true);


        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setUserName("Juan.Perez");

        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(trainer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"Juan.Perez\",\"actionType\":\"add\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("Juan.Perez"));
    }

    @Test
    void whenRegisterTrainerTrainingForDeletionAndTrainerNotFound_thenReturnsNoContent() throws Exception {
        TrainerWorkloadDto dto = new TrainerWorkloadDto();
        dto.setUserName("JohnDoe");
        dto.setActionType("delete");

        when(trainerService.saveOrUpdateUserTraining(any(TrainerWorkloadDto.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"JohnDoe\",\"actionType\":\"delete\"}"))
                .andExpect(status().isNoContent());
    }
}