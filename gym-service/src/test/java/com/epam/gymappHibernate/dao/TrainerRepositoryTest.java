package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb3")
class TrainerRepositoryTest {
    @MockBean
    private EntityManager entityManager;

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;

    @Mock
    private TypedQuery<Trainer> query;

    @BeforeEach
    public void setUp() {}

        @Test
        @Transactional
        public void testSaveAndFindTrainer(){
            User user = new User();
            user.setFirstName("Enrique");
            user.setLastName("Ortega");
            user.setPassword("abc123");
            user.setActive(true);
            user.setUserName("Enrique.Ortega");
            userRepository.saveUser(user);

            Trainer trainer = new Trainer();
            trainer.setUser(user);
            trainerRepository.saveTrainer(trainer);

            entityManager.flush();
            entityManager.clear();

            Trainer savedTrainer = trainerRepository.getTrainerByUsername("Enrique.Ortega");

            assertNotNull(savedTrainer);
            assertEquals("Enrique.Ortega", savedTrainer.getUser().getUserName());
        }

        @Test
        @Transactional
        public void testUpdateTrainer() {
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

            User updatedUser = new User();
            updatedUser.setFirstName("jane");
            updatedUser.setLastName("doe");
            updatedUser.setPassword("def456");
            updatedUser.setActive(true);
            updatedUser.setUserName("jane.doe");
            userRepository.saveUser(updatedUser);
            trainer.setUser(updatedUser);
            trainerRepository.updateTrainer(trainer);

            entityManager.flush();
            entityManager.clear();

            Trainer updatedTrainer = trainerRepository.getTrainerByUsername("jane.doe");

            assertNotNull(updatedTrainer);
            assertEquals("jane.doe", updatedTrainer.getUser().getUserName());
        }
    @Test
    public void testFindAll() {
        when(entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)).thenReturn(query);
        List<Trainer> result = trainerRepository.findAll();
        assertEquals(4, result.size());

    }

    }

