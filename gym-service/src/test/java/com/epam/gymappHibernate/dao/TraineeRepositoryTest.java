package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb2")
class TraineeRepositoryTest {
    @MockBean
    private EntityManager entityManager;

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;

    @Mock
    private TypedQuery<Trainee> query;

    @BeforeEach
    public void setUp() {

    }


    @Test
    @Transactional
    public void testSaveTrainee1() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserName("testuser");
        user.setPassword("password");
        user.setActive(true);

        userRepository.saveUser(user);

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Test St");
        trainee.setUser(user);

        traineeRepository.saveTrainee(trainee);

        Trainee savedTrainee = traineeRepository.getTraineeByUsername(user.getUserName());

        assertNotNull(savedTrainee);
        assertEquals(trainee.getTraineeID(), savedTrainee.getTraineeID());
        assertEquals(trainee.getAddress(), savedTrainee.getAddress());
        assertEquals(trainee.getUser().getUserName(), savedTrainee.getUser().getUserName());
    }

    @Test
    @Transactional
    public void UpdateTraineeTest() {
        // Create and save a new user
        User user = new User();
        user.setFirstName("juan");
        user.setLastName("enrique");
        user.setPassword("asd456ss");
        user.setActive(true);
        user.setUserName("juan.enrique");
        userRepository.saveUser(user);

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Test St");
        trainee.setUser(user);
        traineeRepository.saveTrainee(trainee);

        trainee.setAddress("456 New St");
        traineeRepository.updateTrainee(trainee);

        entityManager.flush();
        entityManager.clear();

        Trainee updatedTrainee = traineeRepository.getTraineeByUsername("juan.enrique");

        assertNotNull(updatedTrainee);
        assertEquals("456 New St", updatedTrainee.getAddress());
    }


    @Test
    @Transactional
    public void DeleteTraineeByUsername1() {
        User user = new User();
        user.setFirstName("alicia");
        user.setLastName("sanchez");
        user.setPassword("asd456");
        user.setActive(true);
        user.setUserName("alicia.sanchez");

        userRepository.saveUser(user);

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Test St");
        trainee.setUser(user);

        traineeRepository.saveTrainee(trainee);
        traineeRepository.deleteTraineeByUsername("alicia.sanchez");

        entityManager.flush();
        entityManager.clear();

        Trainee deletedTrainee = traineeRepository.getTraineeByUsername("alicia.sanchez");
        assertNull(deletedTrainee);
    }


    @Test
    @Transactional
    public void testGetTraineeByUsername() {
        User user = new User();
        user.setFirstName("pedro");
        user.setLastName("garcia");
        user.setPassword("asd456");
        user.setActive(true);
        user.setUserName("pedro.garcia");

        userRepository.saveUser(user);

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Test St");
        trainee.setUser(user);

        traineeRepository.saveTrainee(trainee);
        Trainee savedTrainee = traineeRepository.getTraineeByUsername(user.getUserName());
        assertEquals("pedro", savedTrainee.getUser().getFirstName());
    }


    @Test
    @Transactional
    public void testFindAll() {

        when(entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)).thenReturn(query);
        List<Trainee> result = traineeRepository.findAll();
        assertEquals(6, result.size());

    }

}