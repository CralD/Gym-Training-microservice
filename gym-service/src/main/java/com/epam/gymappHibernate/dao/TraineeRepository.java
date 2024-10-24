package com.epam.gymappHibernate.dao;


import com.epam.gymappHibernate.entity.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TraineeRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(TraineeRepository.class);

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveTrainee(Trainee trainee) {
        entityManager.persist(trainee);
    }

    @Transactional
    public void updateTrainee(Trainee trainee) {
        entityManager.merge(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t WHERE t.user.userName = :username", Trainee.class
            );
            query.setParameter("username", username);
            Trainee trainee = query.getSingleResult();
            entityManager.remove(trainee);
            entityManager.flush();
            entityManager.clear();
            logger.info("Trainee with username {} deleted successfully", username);
        } catch (NoResultException e) {
            logger.error("No trainee found with username: {}", username, e);
        }
    }

    public Trainee getTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t WHERE t.user.userName = :username", Trainee.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Trainee> findAll() {
        TypedQuery<Trainee> query = entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class);
        return query.getResultList();
    }

}






