package com.epam.gymappHibernate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTrainer {
    private List<String> trainerUsernames;
}
