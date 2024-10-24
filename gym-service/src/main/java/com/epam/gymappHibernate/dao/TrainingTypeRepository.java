package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingTypeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public TrainingTypeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TrainingType getTrainingTypeName(String trainingTypeName) {
        try {
            TypedQuery<TrainingType> query = entityManager.createQuery(
                    "SELECT t FROM TrainingType t WHERE t.trainingTypeName = :trainingTypeName", TrainingType.class
            );
            query.setParameter("trainingTypeName", trainingTypeName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<TrainingType> findAll() {
        TypedQuery<TrainingType> query = entityManager.createQuery(
                "SELECT t FROM TrainingType t", TrainingType.class
        );
        return query.getResultList();
    }
}
