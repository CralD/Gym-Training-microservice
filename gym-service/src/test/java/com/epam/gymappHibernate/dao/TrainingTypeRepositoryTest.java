package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb8")
class TrainingTypeRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @BeforeEach
    public void setUp() {
        trainingTypeRepository = new TrainingTypeRepository(entityManager);
    }

    @Test
    @Transactional
    public void testGetTrainingTypeName() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");
        if (trainingTypeRepository.getTrainingTypeName("Cardio") == null) {
            entityManager.persist(trainingType);
            entityManager.flush();
        }
        TrainingType fetchedTrainingType = trainingTypeRepository.getTrainingTypeName("Cardio");
        assertNotNull(fetchedTrainingType);
        assertEquals("Cardio", fetchedTrainingType.getTrainingTypeName());
    }

    @Test
    @Transactional
    public void testGetTrainingTypeName_NotFound() {
        TrainingType fetchedTrainingType = trainingTypeRepository.getTrainingTypeName("NonExistentType");
        assertNull(fetchedTrainingType);
    }
}