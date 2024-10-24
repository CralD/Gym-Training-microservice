package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TRAINER")
@Getter
@Setter
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long trainerId;
    @OneToOne
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    private User user;
    @OneToMany(mappedBy = "trainer")
    private List<Training> trainings;
    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SPECIALIZATION", referencedColumnName = "ID")
    private TrainingType specialization;

    public Trainer() {

    }


}
