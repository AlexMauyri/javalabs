package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CalculationGenericDAOImpl implements GenericDAO<Calculation> {
    private final Logger logger = LogManager.getLogger(CalculationGenericDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Calculation entity) {
        try {
            entityManager.persist(entity);
            logger.info("Created new calculation with ID: {}", entity.getId());
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public Calculation read(int id) {
        Calculation calculation = null;
        try {
            calculation = entityManager.find(Calculation.class, id);
            logger.info("Read calculation with ID: {}", id);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return calculation;
    }

    public List<Calculation> readByHash(long hash) {
        List<Calculation> calculation = null;
        try {
            TypedQuery<Calculation> query = entityManager.createQuery(loadQueryFromFile("readByHash_Calculation.hql"), Calculation.class);
            query.setParameter("value", hash);
            calculation = query.getResultList();
            logger.info("Read calculation with hash: {}", hash);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return calculation;
    }

    @Override
    public List<Calculation> readAll() {
        List<Calculation> calculations = null;
        try {
            calculations = entityManager.createQuery(loadQueryFromFile("readAll_Calculation.hql"), Calculation.class).getResultList();
            logger.info("Read all calculations");
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return calculations;
    }

    public List<Calculation> readAll(Double appliedValue, Double resultValue,
                                     Operations operationX, Operations operationY,
                                     Sorting sortingX, Sorting sortingY) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Calculation> query = criteriaBuilder.createQuery(Calculation.class);
        Root<Calculation> root = query.from(Calculation.class);

        List<Predicate> predicates = new ArrayList<>();
        if (appliedValue != null && operationX != null) {
            doOperation(predicates, root, criteriaBuilder, operationX, appliedValue, "appliedX");
        }
        if (resultValue != null && operationY != null) {
            doOperation(predicates, root, criteriaBuilder, operationY, resultValue, "resultY");
        }

        if (sortingX != null) doSort(query, root, criteriaBuilder, "appliedX", sortingX);
        if (sortingY != null) doSort(query, root, criteriaBuilder, "resultY", sortingY);

        query = query.where(predicates.toArray(Predicate[]::new));

        List<Calculation> result;
        try {
            result = entityManager.createQuery(query).getResultList();
            logger.info("Read all calculations with sorting by: {}, {}, {}, {}, {}, {}",
                    appliedValue,
                    resultValue,
                    operationX != null ? operationX.toString() : null,
                    operationY != null ? operationY.toString() : null,
                    sortingX != null ? sortingX.toString() : null,
                    sortingY != null ? sortingY.toString() : null);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
        return result;
    }

    private void doOperation(List<Predicate> predicates, Root<Calculation> root,
                             CriteriaBuilder criteriaBuilder, Operations operation,
                             double value, String field) {
        switch (operation) {
            case equal -> predicates.add(criteriaBuilder.equal(root.get(field), value));
            case lessThen -> predicates.add(criteriaBuilder.lessThan(root.get(field), value));
            case greaterThen -> predicates.add(criteriaBuilder.greaterThan(root.get(field), value));
            case greaterOrEqual -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(field), value));
            case lessOrEqual -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(field), value));
            case notEqual -> predicates.add(criteriaBuilder.notEqual(root.get(field), value));
        }
    }

    private void doSort(CriteriaQuery<Calculation> query, Root<Calculation> root,
                        CriteriaBuilder criteriaBuilder, String field, Sorting sorting) {
        switch (sorting) {
            case ASCENDING -> query.orderBy(criteriaBuilder.asc(root.get(field)));
            case DESCENDING -> query.orderBy(criteriaBuilder.desc(root.get(field)));
        }
    }

    @Override
    public void update(Calculation entity) {
        try {
            entityManager.merge(entity);
            logger.info("Update Calculation with ID: {}. New values: {}, {}, {}",
                    entity.getId(),
                    entity.getAppliedX(),
                    entity.getResultY(),
                    entity.getHash());
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        try {
            entityManager.remove(entityManager.find(Calculation.class, id));
            logger.info("Delete Calculation with ID: {}", id);
        } catch (RuntimeException e) {
            logger.error(e);
            throw e;
        }
    }
}
