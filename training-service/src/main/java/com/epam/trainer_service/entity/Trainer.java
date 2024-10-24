package com.epam.trainer_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name= "TRAINER")
@Getter
@Setter
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean isActive;

    @ElementCollection
    @CollectionTable(name = "training_details", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "year_month")
    @Column(name = "summary_duration")
    private Map<String, Double> trainingSummary;
}
