package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb4")
class TrainingRepositoryTest {
    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testSaveTraining() {
        Training training = new Training();
        training.setTrainingName("cardio");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);

        trainingRepository.saveTraining(training);


        entityManager.flush();
        entityManager.clear();

        Training savedTraining = entityManager.find(Training.class, training.getTrainingId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expectedDate = sdf.format(training.getTrainingDate());
        String actualDate = sdf.format(savedTraining.getTrainingDate());

        assertNotNull(savedTraining);
        assertEquals("cardio", savedTraining.getTrainingName());
        assertEquals(60, savedTraining.getTrainingDuration());
        assertEquals(expectedDate, actualDate);
    }

    @Test
    @Transactional
    public void testSaveAndFindTrainings() {

        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPassword("abc123");
        user.setActive(true);
        user.setUserName("john.doe");
        userRepository.saveUser(user);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainerRepository.saveTrainer(trainer);

        User user2 = new User();
        user2.setFirstName("Paolo");
        user2.setLastName("Guerrero");
        user2.setPassword("awwww1444");
        user2.setActive(true);
        user2.setUserName("Paolo.Guerrero");
        userRepository.saveUser(user2);

        Trainee trainee = new Trainee();
        trainee.setUser(user2);
        traineeRepository.saveTrainee(trainee);

        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingName("cardio");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);
        training.setTrainee(trainee);
        trainingRepository.saveTraining(training);


        entityManager.flush();
        entityManager.clear();

        List<Training> savedTrainings = trainingRepository.getTrainerTrainings("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), new Date(), "Paolo");

        assertNotNull(savedTrainings);
        Training savedTraining = savedTrainings.get(0);
        assertEquals("john.doe", savedTraining.getTrainer().getUser().getUserName());
        assertEquals("Paolo.Guerrero", savedTraining.getTrainee().getUser().getUserName());
    }

    @Test
    @Transactional
    public void testSaveAndFindTrainingsForTrainees() {

        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPassword("abc123");
        user.setActive(true);
        user.setUserName("john.doe");
        userRepository.saveUser(user);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        traineeRepository.saveTrainee(trainee);

        User user2 = new User();
        user2.setFirstName("Paolo");
        user2.setLastName("Guerrero");
        user2.setPassword("awwww1444");
        user2.setActive(true);
        user2.setUserName("Paolo.Guerrero");
        userRepository.saveUser(user2);

        Trainer trainer = new Trainer();
        trainer.setUser(user2);
        trainerRepository.saveTrainer(trainer);

        TrainingType trainingType = trainingTypeRepository.getTrainingTypeName("Cardio");

        assertNotNull(trainingType);
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingName("cardio training");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);
        training.setTrainee(trainee);
        training.setTrainingType(trainingType);
        trainingRepository.saveTraining(training);

        entityManager.flush();
        entityManager.clear();
        List<Training> savedTrainings = trainingRepository.getTraineeTrainings("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), new Date(), "Paolo", "Cardio");
        assertNotNull(savedTrainings);
        Training savedTraining = savedTrainings.get(0);
        assertEquals("Paolo", savedTraining.getTrainer().getUser().getFirstName());
        assertEquals("Paolo.Guerrero", savedTraining.getTrainer().getUser().getUserName());
    }

}