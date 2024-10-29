package com.epam.gymappHibernate.util;

import com.epam.gymappHibernate.dto.TrainerWorkloadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trainer-service")
public interface TrainingServiceMicrosService {

    @PostMapping("/api/trainers")
    ResponseEntity<?> registerTrainerTraining(@RequestBody TrainerWorkloadDto trainerWorkloadDto);

    @GetMapping("/api/trainers/{username}/{yearMonth}")
    ResponseEntity<Double> getTotalHoursForMonth(@PathVariable String username, @PathVariable String yearMonth);

}
