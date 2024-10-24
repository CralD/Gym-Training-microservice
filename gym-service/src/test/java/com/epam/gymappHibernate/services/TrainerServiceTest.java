package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.TrainingTypeRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.dto.TrainerDto;
import com.epam.gymappHibernate.dto.TrainerDtoResponse;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb6")
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrainerService trainerService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Trainer trainer;
    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    private TrainerDto trainerDto;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("Pedro");
        user.setLastName("Garcia");
        user.setUserName("Pedro.Garcia");
        user.setPassword("password");

        trainer = new Trainer();
        trainer.setUser(user);
    }



    @Test
    public void testAuthenticateSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        boolean result = trainerService.authenticate(username, password);
        assertTrue(result);
    }

    @Test
    public void testAuthenticateFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        boolean result = trainerService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        String username = "unknown";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(null);
        boolean result = trainerService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testGetTrainerByUsernameSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        Trainer result = trainerService.getTrainerByUsername(username);
        assertEquals(trainer, result);
    }



    @Test
    public void testUpdateTrainerProfileSuccess() {
        String username = "Pedro.Garcia";

        TrainerDto trainerDto = new TrainerDto();
        trainerDto.setFirstName("Pedro");
        trainerDto.setLastName("Garcia");
        trainerDto.setActive(true);
        trainerDto.setSpecialization("Cardio");

        Trainer trainer = new Trainer();
        User user = new User();
        user.setUserName(username);
        user.setPassword("password");
        trainer.setUser(user);

        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        when(trainingTypeRepository.getTrainingTypeName("Cardio")).thenReturn(new TrainingType()); // Mock the specialization lookup

        trainerService.updateTrainerProfile(username, trainerDto);

        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }



    @Test
    public void testChangeTrainerPasswordSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        String newPassword = "newpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        trainerService.changeTrainerPassword(username, newPassword, password);
        assertEquals(newPassword, trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }



    @Test
    public void testSetTrainerActiveStatusSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        boolean isActive = true;
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        trainerService.setTrainerActiveStatus(username, isActive);
        assertEquals(isActive, trainer.getUser().isActive());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }



    @Test
    public void testFindUnassignedTrainers() {
        String traineeUsername = "trainee1";


        TrainingType specialization = new TrainingType();
        specialization.setTrainingTypeName("Specialization");

        Trainer trainer = new Trainer();
        trainer.setTrainerId(1L);
        trainer.setSpecialization(specialization);
        trainer.setUser(user);

        List<Trainer> unassignedTrainers = Collections.singletonList(trainer);
        when(trainerRepository.findUnassignedTrainers(traineeUsername)).thenReturn(unassignedTrainers);


        List<TrainerDtoResponse> result = trainerService.findUnassignedTrainers(traineeUsername);


        assertNotNull(result);
        assertEquals(1, result.size());


        verify(trainerRepository, times(1)).findUnassignedTrainers(traineeUsername);
    }

}