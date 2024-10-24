package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.services.TrainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Api(tags = "Users")
public class UserController {
    private final TraineeService traineeService;

    private final TrainerService trainerService;
    @Autowired
    public UserController(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @GetMapping("/login")
    @ApiOperation(value = "User login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<String> login(
            @RequestHeader(name = "username") String username,
            @RequestHeader(name = "password") String password) {


        if (traineeService.authenticate(username, password) || trainerService.authenticate(username, password)) {
            return new ResponseEntity<>("Welcome", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/{username}/password")
    @ApiOperation(value = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password updated successfully"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    public ResponseEntity<String> updatePassword(
            @PathVariable("username") String username,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("password") String password) {

        if (traineeService.authenticate(username, password)) {
            traineeService.changeTraineePassword(username, newPassword, password);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        }  else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}
