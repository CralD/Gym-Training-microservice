package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.TrainingTypeRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.dto.*;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.util.PasswordGenerator;
import com.epam.gymappHibernate.util.UsernameGenerator;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainerService {
    @Autowired
    private final TrainerRepository trainerRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    @Autowired
    private JmsTemplate jmstemplate;

    private static final String HOURS_QUEUE = "trainer.hour.queue";

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserRepository userRepository, TrainingTypeRepository trainingTypeRepository, TraineeRepository traineeRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.traineeRepository = traineeRepository;
    }

    @Transactional
    public CredentialsDto createTrainer(TrainerDto request) {
        Trainer trainer = mapToTrainer(request);

        List<String> existingUsernames = userRepository.getAllUsers();
        String username = UsernameGenerator.generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName(), existingUsernames);
        trainer.getUser().setUserName(username);
        String password = PasswordGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(password);
        trainer.getUser().setPassword(encodedPassword);
        trainer.getUser().setActive(true);
        userRepository.saveUser(trainer.getUser());
        TrainingType existingTrainingType = trainingTypeRepository.getTrainingTypeName(trainer.getSpecialization().getTrainingTypeName());
        if (existingTrainingType != null) {
            trainer.setSpecialization(existingTrainingType);
        } else {
            throw new IllegalArgumentException("Training type does not exist");
        }


        trainerRepository.saveTrainer(trainer);
        logger.info("Trainer created: {} ", username);
        return new CredentialsDto(username, password);
    }

    public boolean authenticate(String username, String password) {
        Trainer trainer = trainerRepository.getTrainerByUsername(username);
        if (trainer != null && trainer.getUser().getPassword().equals(password)) {
            logger.info("Authentication successful for user: {}", username);
            return true;
        } else {
            logger.warn("Authentication failed for user: {}", username);
            return false;
        }
    }

    public Trainer getTrainerByUsername(String username) {

        logger.info("Selecting Trainer profile: {}", username);
        return trainerRepository.getTrainerByUsername(username);


    }

    @Transactional
    public Trainer updateTrainerProfile(String username, TrainerDto trainerDto) {

        logger.info("Updating Trainer profile: {}", username);

        Trainer trainer = getTrainerByUsername(username);

        trainer.getUser().setFirstName(trainerDto.getFirstName());
        trainer.getUser().setLastName(trainerDto.getLastName());
        trainer.getUser().setActive(trainerDto.isActive());
        if (trainerDto.getSpecialization() != null) {
            TrainingType existingTrainingType = trainingTypeRepository.getTrainingTypeName(trainerDto.getSpecialization());
            if (existingTrainingType != null) {
                trainer.setSpecialization(existingTrainingType);
            } else {
                throw new IllegalArgumentException("Training type does not exist");
            }
        }

        trainerRepository.updateTrainer(trainer);
        return trainer;

    }

    @Transactional
    public void setTrainerActiveStatus(String username, boolean isActive) {
        logger.info("Setting active status for trainer: {}", username);

        Trainer trainer = trainerRepository.getTrainerByUsername(username);


        if (trainer != null) {

            trainer.getUser().setActive(isActive);

            trainerRepository.updateTrainer(trainer);

            logger.info("Active status for trainer {} set to {}", username, isActive);
        } else {
            logger.warn("Trainer {} not found", username);
        }

    }

    @Transactional
    public void changeTrainerPassword(String username, String newPassword, String password) {

        Trainer trainer = trainerRepository.getTrainerByUsername(username);
        if (trainer != null) {
            logger.info("Changing Password");
            trainer.getUser().setPassword(newPassword);
            trainerRepository.updateTrainer(trainer);
        }

    }


    public List<TrainerDtoResponse> findUnassignedTrainers(String traineeUsername) {
        logger.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        List<Trainer> unassignedTrainers = trainerRepository.findUnassignedTrainers(traineeUsername);
        return unassignedTrainers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TrainerDtoResponse> updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        Trainee trainee = traineeRepository.getTraineeByUsername(traineeUsername);
        if (trainee == null) {
            throw new IllegalArgumentException("Trainee not found");
        }


        // List<Trainer> trainers = trainerRepository.getTrainerByUsername(trainerUsernames);
        Set<Trainer> trainers = trainerUsernames.stream()
                .map(trainerRepository::getTrainerByUsername)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (trainers.isEmpty()) {
            throw new IllegalArgumentException("No valid trainers found for the given usernames");
        }
        trainee.setTrainers(trainers);
        traineeRepository.saveTrainee(trainee);

        return trainers.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @CircuitBreaker(name = "TrainingService")
    public void getTotalTrainingHoursForMonth(String username, String yearMonth) {
        Map<String, String> messagePayload = new HashMap<>();
        messagePayload.put("username", username);
        messagePayload.put("yearMonth", yearMonth);
        jmstemplate.convertAndSend(HOURS_QUEUE, messagePayload);
        logger.info("JMS message sent for username: {} and yearMonth: {}", username, yearMonth);
    }


    public Trainer mapToTrainer(TrainerDto trainerDto) {
        User user = new User();
        user.setFirstName(trainerDto.getFirstName());
        user.setLastName(trainerDto.getLastName());

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        if (trainerDto.getSpecialization() != null) {
            TrainingType trainingType = new TrainingType();
            trainingType.setTrainingTypeName(trainerDto.getSpecialization());
            trainer.setSpecialization(trainingType);
        }
        return trainer;
    }

    public TrainerDto trainerDtoConverter(Trainer trainer) {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName(trainer.getUser().getFirstName());
        trainerDto.setLastName(trainer.getUser().getLastName());
        trainerDto.setSpecialization(trainer.getSpecialization() != null ? trainer.getSpecialization().getTrainingTypeName() : null);
        trainerDto.setActive(trainer.getUser().isActive());

        List<TraineeDtoResponse> trainees = trainer.getTrainees().stream().map(trainee -> {
            TraineeDtoResponse traineeDto = new TraineeDtoResponse();
            traineeDto.setUserName(trainee.getUser().getUserName());
            traineeDto.setFirstName(trainee.getUser().getFirstName());
            traineeDto.setLastName(trainee.getUser().getLastName());
            return traineeDto;
        }).collect(Collectors.toList());

        trainerDto.setTrainees(trainees);
        return trainerDto;
    }

    private TrainerDtoResponse convertToDto(Trainer trainer) {
        TrainerDtoResponse dto = new TrainerDtoResponse();
        dto.setUsername(trainer.getUser().getUserName());
        dto.setFirstname(trainer.getUser().getFirstName());
        dto.setLastname(trainer.getUser().getLastName());
        dto.setSpecialization(trainer.getSpecialization().getTrainingTypeName());
        return dto;
    }


}
