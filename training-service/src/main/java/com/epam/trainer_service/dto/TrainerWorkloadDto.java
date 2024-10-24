package com.epam.trainer_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class TrainerWorkloadDto {

    private String userName;
    private String firstName;
    private String lastName;
    private boolean status;
    private String trainingDate;
    private double trainingDuration;
    private String actionType;
}
