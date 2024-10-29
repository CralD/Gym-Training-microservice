package com.epam.trainer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadDto {

    private String userName;
    private String firstName;
    private String lastName;
    private boolean status;
    private String trainingDate;
    private double trainingDuration;
    private String actionType;
}
