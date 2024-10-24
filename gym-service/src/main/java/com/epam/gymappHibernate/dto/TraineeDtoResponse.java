package com.epam.gymappHibernate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TraineeDtoResponse {
    private String userName;
    private String firstName;
    private String lastName;

}
