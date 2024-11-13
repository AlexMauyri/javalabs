package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;

import java.util.List;

@Repository
public class AppliedFunctionGenericDAOImpl implements GenericDAO<AppliedFunction> {
    private final Logger logger = LogManager.getLogger(AppliedFunctionGenericDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AppliedFunction read(int id) {
        AppliedFunction appliedFunction = null;
        try {
            appliedFunction = entityManager.find(AppliedFunction.class, id);
            logger.info("Read AppliedFunction with ID: {}", id);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return appliedFunction;
    }

    public List<AppliedFunction> readByCalculationId(int id) {
        List<AppliedFunction> appliedFunction = null;
        try {
            TypedQuery<AppliedFunction> query = entityManager.createQuery(loadQueryFromFile("readByCalculationId_AppliedFunction.hql"), AppliedFunction.class);
            query.setParameter("value", id);
            appliedFunction = query.getResultList();
            logger.info("Read AppliedFunction with Calculation ID: {}", id);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return appliedFunction;
    }

    @Override
    public List<AppliedFunction> readAll() {
        List<AppliedFunction> appliedFunction = null;
        try {
            appliedFunction = entityManager.createQuery(loadQueryFromFile("readAll_AppliedFunction.hql"), AppliedFunction.class).getResultList();
            logger.info("Read All AppliedFunction");
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return appliedFunction;
    }

    @Override
    public void create(AppliedFunction entity) {
        try {
            entityManager.persist(entity);
            logger.info("Create AppliedFunction with ID: {}", entity.getId());
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void update(AppliedFunction entity) {
        try {
            entityManager.merge(entity);
            logger.info("Update AppliedFunction with ID: {}. New values: {}, {}, {}, {}, {}",
                    entity.getId(),
                    entity.getCalculationId().getId(),
                    entity.getFunctionOrder(),
                    entity.getFunctionSerialized(),
                    entity.getModUnmodifiable(),
                    entity.getModStrict());
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        try {
            entityManager.remove(entityManager.find(AppliedFunction.class, id));
            logger.info("Delete AppliedFunction with ID: {}", id);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }
}
