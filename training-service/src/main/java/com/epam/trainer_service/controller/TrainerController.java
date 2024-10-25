package com.epam.trainer_service.controller;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/trainers")
public class TrainerController {
    private final Logger logInfo = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    String transactionId = UUID.randomUUID().toString();

    @PostMapping
    public ResponseEntity<?> registerTrainerTraining( @RequestBody TrainerWorkloadDto trainerWorkloadDto){

        logInfo.info("Transaction ID: " + transactionId + " - Endpoint called: POST /registerTrainerTraining, Request: " + trainerWorkloadDto);
        Trainer trainerTraining = trainerService.saveOrUpdateUserTraining(trainerWorkloadDto);
        if (trainerTraining == null && "delete".equalsIgnoreCase(trainerWorkloadDto.getActionType())) {
            logInfo.info("Transaction ID: " + transactionId + " - No trainer found for deletion");
            return ResponseEntity.noContent().build();
        }
        logInfo.info("Transaction ID: " + transactionId + " - Training registered: " + trainerTraining);

        return ResponseEntity.ok(trainerTraining);
    }

    @GetMapping("/{username}/{yearMonth}")

    public ResponseEntity<Double> getTotalHoursForMonth(@PathVariable String username, @PathVariable String yearMonth) {

        double totalHours = trainerService.getTotalHoursForMonth(username, yearMonth);
        logInfo.info("Transaction ID: " + transactionId + " - Total hours for " + username + " in " + yearMonth + ": " + totalHours);
        return ResponseEntity.ok(totalHours);
    }



}

