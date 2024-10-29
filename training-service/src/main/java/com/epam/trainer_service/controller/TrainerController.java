package com.epam.trainer_service.controller;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.service.TrainerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
    @ApiOperation(value = "Register or update trainer training details", response = Trainer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered the training"),
            @ApiResponse(code = 204, message = "No trainer found for deletion"),

    })
    public ResponseEntity<?> registerTrainerTraining(@RequestBody TrainerWorkloadDto trainerWorkloadDto) {

        logInfo.info("Transaction ID: " + transactionId + " - Endpoint called: POST /registerTrainerTraining, Request: " + trainerWorkloadDto);
        Optional<Trainer> trainerTraining = trainerService.saveOrUpdateUserTraining(trainerWorkloadDto);
        if (trainerTraining.isEmpty() && "delete".equalsIgnoreCase(trainerWorkloadDto.getActionType())) {
            logInfo.info("Transaction ID: " + transactionId + " - No trainer found for deletion");
            return ResponseEntity.noContent().build();
        }
        logInfo.info("Transaction ID: " + transactionId + " - Training registered: " + trainerTraining);

        return ResponseEntity.ok(trainerTraining);
    }

    @GetMapping("/{username}/{yearMonth}")
    @ApiOperation(value = "Get total training hours for a specific month", response = Double.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the total training hours"),
            @ApiResponse(code = 400, message = "Invalid input, object invalid"),

    })
    public ResponseEntity<Optional<Double>> getTotalHoursForMonth(@PathVariable String username, @PathVariable String yearMonth) {

        Optional<Double> totalHours = trainerService.getTotalHoursForMonth(username, yearMonth);
        logInfo.info("Transaction ID: " + transactionId + " - Total hours for " + username + " in " + yearMonth + ": " + totalHours);
        return ResponseEntity.ok(totalHours);
    }


}

