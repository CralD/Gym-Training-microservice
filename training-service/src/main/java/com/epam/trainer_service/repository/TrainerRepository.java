package com.epam.trainer_service.repository;

import com.epam.trainer_service.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository  extends JpaRepository<Trainer,Long> {
        Trainer findByUserName(String username);
}
