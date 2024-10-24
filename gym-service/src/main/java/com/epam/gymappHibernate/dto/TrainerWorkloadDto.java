package com.epam.gymappHibernate.dto;

import lombok.Getter;
import lombok.Setter;

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
