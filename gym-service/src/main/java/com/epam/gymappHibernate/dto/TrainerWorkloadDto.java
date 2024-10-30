package com.epam.gymappHibernate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerWorkloadDto {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("trainingDate")
    private String trainingDate;
    @JsonProperty("trainingDuration")
    private double trainingDuration;
    @JsonProperty("actionType")
    private String actionType;
}
