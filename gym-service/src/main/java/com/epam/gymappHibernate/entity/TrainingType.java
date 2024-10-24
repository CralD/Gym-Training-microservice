package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "TRAININGTYPE")
@Getter
@Setter
public class TrainingType {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long trainingTypeId;
    @Column(name = "TYPENAME")
    private String trainingTypeName;
    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings;
    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers;


    public TrainingType() {

    }


}
