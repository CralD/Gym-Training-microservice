package com.epam.trainer_service.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;
import java.util.Map;

@Document
@Getter
@Setter
public class Trainer {
    @Id
    private String trainerId;

    private String userName;
    @Indexed
    private String firstName;
    @Indexed
    private String lastName;
    private boolean isActive;


    private Map<String, List<Double>> trainingSummary;
}
