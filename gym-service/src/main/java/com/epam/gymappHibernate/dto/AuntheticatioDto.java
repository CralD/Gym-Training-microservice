package com.epam.gymappHibernate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuntheticatioDto {
    private String password;
    private boolean isActive;
}
