package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.TrainingRepository;
import com.epam.gymappHibernate.dao.TrainingTypeRepository;
import com.epam.gymappHibernate.dto.TrainerWorkloadDto;
import com.epam.gymappHibernate.dto.TrainingDto;
import com.epam.gymappHibernate.dto.TrainingDtoResponse;
import com.epam.gymappHibernate.dto.TrainingTypeDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.Training;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.util.TrainingServiceMicrosService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TrainingService {

    private TrainingRepository trainingRepository;
    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;
    private TrainingServiceMicrosService trainingMicroservice;

    private static final String TRAINING_QUEUE = "training.add.queue";

    private JmsTemplate jmsTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TrainerRepository trainerRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository, TrainingServiceMicrosService trainingMicroservice,JmsTemplate jmsTemplate) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingMicroservice = trainingMicroservice;
        this.jmsTemplate = jmsTemplate;
    }

    String transactionId = UUID.randomUUID().toString();

    @Transactional
    public void saveTraining(Training training) {
        trainingRepository.saveTraining(training);
        logger.info("Training saved: {}", training.getTrainingId());
    }

    @Transactional
    @CircuitBreaker(name = "TrainingService")
    public void addTraining(String Traineeusername, String Trainerusername, TrainingDto request) {

        Trainee trainee = traineeRepository.getTraineeByUsername(Traineeusername);

        Trainer trainer = trainerRepository.getTrainerByUsername(Trainerusername);
        Date date = request.getTrainingDate();
        ZoneId systemDefault = ZoneId.systemDefault();
        OffsetDateTime offsetDateTime = date.toInstant().atZone(systemDefault).toOffsetDateTime();


        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String isoDate = formatter.format(offsetDateTime);

        TrainerWorkloadDto dto = new TrainerWorkloadDto();
        dto.setUserName(trainer.getUser().getUserName());
        dto.setFirstName(trainer.getUser().getFirstName());
        dto.setLastName(trainer.getUser().getLastName());
        dto.setStatus(true);
        dto.setTrainingDate(isoDate);
        dto.setTrainingDuration(request.getTrainingDuration());
        dto.setActionType("ADD");


        Training training = new Training();
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setTrainingDuration(request.getTrainingDuration());
        training.setTrainingType(trainer.getSpecialization());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        jmsTemplate.convertAndSend(TRAINING_QUEUE, dto);
        trainingRepository.saveTraining(training);

        logger.info("Transaction ID: " + transactionId + "Training added: {}", training.getTrainingName());
    }

    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        logger.info("Fetching trainings for trainee: {}, fromDate: {}, toDate: {}, trainerName: {}, trainingType: {}",
                username, fromDate, toDate, trainerName, trainingType);
        return trainingRepository.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        logger.info("Fetching trainings for trainer: {}, fromDate: {}, toDate: {}, traineeName: {}",
                username, fromDate, toDate, traineeName);
        return trainingRepository.getTrainerTrainings(username, fromDate, toDate, traineeName);
    }

    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream().map(trainingType -> {
            TrainingTypeDto dto = new TrainingTypeDto();
            dto.setId(trainingType.getTrainingTypeId());
            dto.setTrainingTypeName(trainingType.getTrainingTypeName());
            return dto;
        }).collect(Collectors.toList());
    }

    public TrainingDtoResponse convertToDto(Training training) {
        TrainingDtoResponse dto = new TrainingDtoResponse();
        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDate(training.getTrainingDate());
        dto.setTrainingType(training.getTrainingType().getTrainingTypeName());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTrainerName(training.getTrainer().getUser().getUserName());
        return dto;
    }
}
