package com.epam.trainer_service.repository;

import com.epam.trainer_service.entity.Trainer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends MongoRepository<Trainer,String> {
    List<Trainer> findByUserName(String username);
}
