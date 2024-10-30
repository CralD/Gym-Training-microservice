package com.epam.trainer_service.service;

import com.epam.trainer_service.dto.TrainerWorkloadDto;
import com.epam.trainer_service.entity.Trainer;
import com.epam.trainer_service.repository.TrainerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainerService {
    private static final Logger logInfo = LoggerFactory.getLogger(TrainerService.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    private TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    @JmsListener(destination = "trainee.delete.queue,training.add.queue")
    public Optional<Trainer> saveOrUpdateUserTraining(TrainerWorkloadDto request) {
        Trainer userTraining;
        String formattedDate = null;
        if ("add".equalsIgnoreCase(request.getActionType()) && request.getTrainingDate() != null) {
            try {
                OffsetDateTime dateTime = OffsetDateTime.parse(request.getTrainingDate()); // Directly parse the ISO 8601 string
                formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yy-MM"));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use the ISO 8601 date format (e.g., 2024-09-15T00:00:00.000+00:00).");
            }
        }

        if ("add".equalsIgnoreCase(request.getActionType())) {
            userTraining = new Trainer();
            userTraining.setUserName(request.getUserName());
            userTraining.setFirstName(request.getFirstName());
            userTraining.setLastName(request.getLastName());
            userTraining.setActive(request.isStatus());

            Map<String, Double> trainingDetails = new HashMap<>();
            if (formattedDate != null) {
                trainingDetails.put(formattedDate, request.getTrainingDuration());
                userTraining.setTrainingSummary(trainingDetails);
            }
            logInfo.info("Trainer training Saved");
            return Optional.of(trainerRepository.save(userTraining));
        } else if ("delete".equalsIgnoreCase(request.getActionType())) {
            userTraining = trainerRepository.findByUserName(request.getUserName());
            if (userTraining != null) {
                trainerRepository.deleteById(userTraining.getTrainerId());
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @JmsListener(destination = "trainer.hour.queue")
    public Optional<Double> getTotalHoursForMonth(String username, String yearMonth) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }

        Trainer trainer = trainerRepository.findByUserName(username);
        if (trainer != null) {
            Map<String, Double> trainingDetails = trainer.getTrainingSummary();
            logInfo.info("Total hours for Trainer this month");
            return Optional.ofNullable(trainingDetails.get(yearMonth));
        }

        return Optional.empty();
    }
}
