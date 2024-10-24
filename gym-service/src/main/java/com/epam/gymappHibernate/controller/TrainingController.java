package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dto.TrainingDto;
import com.epam.gymappHibernate.dto.TrainingTypeDto;
import com.epam.gymappHibernate.exception.InvalidTrainingDataException;
import com.epam.gymappHibernate.exception.NoTrainingTypesFoundException;
import com.epam.gymappHibernate.services.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@Api(tags = "Trainings")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingService trainingTypeService;

    @PostMapping("/add")
    @ApiOperation(value = "Add a training")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training added successfully"),
            @ApiResponse(code = 400, message = "Invalid training data")
    })
    public ResponseEntity<Void> addTraining(@RequestBody TrainingDto request) {
        try {
            trainingService.addTraining(request.getTraineeUsername(), request.getTrainerUsername(), request);
        } catch (Exception e) {
            throw new InvalidTrainingDataException("Invalid training data provided");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainings")
    @ApiOperation(value = "Get training types")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training types found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<List<TrainingTypeDto>> getTrainingTypes() {
        List<TrainingTypeDto> trainingTypes = trainingTypeService.getAllTrainingTypes();
        if (trainingTypes.isEmpty()) {
            throw new NoTrainingTypesFoundException("No training types found");
        }
        return ResponseEntity.ok(trainingTypes);
    }

}
