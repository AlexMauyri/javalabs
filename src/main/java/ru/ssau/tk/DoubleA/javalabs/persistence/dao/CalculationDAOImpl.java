package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.ssau.tk.DoubleA.javalabs.persistence.Operations;
import ru.ssau.tk.DoubleA.javalabs.persistence.Sorting;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.ArrayList;
import java.util.List;

public class CalculationDAOImpl implements DAO<Calculation> {
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    EntityManager entityManager = sessionFactory.openSession();

    @Override
    public void create(Calculation entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public Calculation read(int id) {
        Calculation calculation = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            calculation = session.get(Calculation.class, id);
            session.getTransaction().commit();
        }
        return calculation;
    }

    public List<Calculation> readByHash(long hash) {
        List<Calculation> calculation = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<Calculation> query = session.createQuery(loadQueryFromFile("readByHash_Calculation.hql"), Calculation.class);
            query.setParameter("value", hash);
            calculation = query.list();

            session.getTransaction().commit();
        }
        return calculation;
    }

    @Override
    public List<Calculation> readAll() {
        List<Calculation> calculations = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            calculations = session.createQuery(loadQueryFromFile("readAll_Calculation.hql"), Calculation.class).list();
            session.getTransaction().commit();
        }
        return calculations;
    }

    public List<Calculation> readAll(Double appliedValue, Double resultValue, Operations operationX, Operations operationY, Sorting sortingX, Sorting sortingY) {
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
        return entityManager.createQuery(query).getResultList();
    }

    private void doOperation(List<Predicate> predicates, Root<Calculation> root, CriteriaBuilder criteriaBuilder, Operations operation, double value, String field) {
        switch (operation) {
            case equal -> predicates.add(criteriaBuilder.equal(root.get(field), value));
            case lessThen -> predicates.add(criteriaBuilder.lessThan(root.get(field), value));
            case greaterThen -> predicates.add(criteriaBuilder.greaterThan(root.get(field), value));
            case greaterOrEqual -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(field), value));
            case lessOrEqual -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(field), value));
            case notEqual -> predicates.add(criteriaBuilder.notEqual(root.get(field), value));
        }
    }

    private void doSort(CriteriaQuery<Calculation> query, Root<Calculation> root, CriteriaBuilder criteriaBuilder, String field, Sorting sorting) {
        switch (sorting) {
            case ASCENDING -> query.orderBy(criteriaBuilder.asc(root.get(field)));
            case DESCENDING -> query.orderBy(criteriaBuilder.desc(root.get(field)));
        }
    }

    @Override
    public void update(Calculation entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.get(Calculation.class, id));
            session.getTransaction().commit();
        }
    }
}
