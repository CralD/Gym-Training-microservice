package com.epam.trainer_service.controller;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trainers")
public class TrainerController {
    private final Logger logInfo = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    public ResponseEntity<?> registerTrainerTraining( @RequestBody TrainerWorkloadDto trainerWorkloadDto){
                Trainer trainerTraining = trainerService.saveOrUpdateUserTraining(trainerWorkloadDto);
        if (trainerTraining == null && "delete".equalsIgnoreCase(trainerWorkloadDto.getActionType())) {
            logInfo.info("no trainer found");
            return ResponseEntity.noContent().build();
        }
        logInfo.info("Training registered");
        return ResponseEntity.ok(trainerTraining);
    }

    @GetMapping("/{username}/{yearMonth}")
    public ResponseEntity<Double> getTotalHoursForMonth(@PathVariable String username, @PathVariable String yearMonth) {
        double totalHours = trainerService.getTotalHoursForMonth(username, yearMonth);
        logInfo.info("the trainer total hours are:");
        return ResponseEntity.ok(totalHours);
    }



}

