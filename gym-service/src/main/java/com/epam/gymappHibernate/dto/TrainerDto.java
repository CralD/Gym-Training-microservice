package com.epam.gymappHibernate.dto;

import com.epam.gymappHibernate.entity.Trainee;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerDto {

    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeDtoResponse> trainees;
}
