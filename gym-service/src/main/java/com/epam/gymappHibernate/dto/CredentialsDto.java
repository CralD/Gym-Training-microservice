package com.epam.gymappHibernate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CredentialsDto {
    private String username;
    private String password;

    public CredentialsDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
