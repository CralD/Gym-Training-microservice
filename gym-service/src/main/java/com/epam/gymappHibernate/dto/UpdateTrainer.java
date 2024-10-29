package com.epam.gymappHibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateTrainer {
    private List<String> trainerUsernames;
}
