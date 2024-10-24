package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "TRAINING")
@Getter
@Setter
public class Training {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long trainingId;
    @Column(name = "TRAININGNAME", nullable = false)
    private String trainingName;
    @ManyToOne
    @JoinColumn(name = "TRAININGTYPEID", referencedColumnName = "ID")
    private TrainingType trainingType;
    @Column(name = "TRAININGDATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date trainingDate;
    @Column(name = "TRAININGDURATION", nullable = false)
    private int trainingDuration;

    @ManyToOne
    @JoinColumn(name = "TRAINEEID", referencedColumnName = "ID")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "TRAINERID", referencedColumnName = "ID")
    private Trainer trainer;

    public Training() {

    }

    public Training(String trainingName, TrainingType trainingType, Date trainingDate, int trainingDuration, Trainee trainee, Trainer trainer) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainee = trainee;
        this.trainer = trainer;
    }
}
