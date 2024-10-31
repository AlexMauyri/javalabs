package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.List;
import java.util.Optional;

public class CalculationDAOImpl implements DAO<Calculation> {
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Optional<Calculation> read(int id) {
        Calculation calculation = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            calculation = session.get(Calculation.class, id);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(calculation);
    }

    @Override
    public List<Calculation> readAll() {
        List<Calculation> calculations = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            calculations = session.createQuery("from Calculation", Calculation.class).list();
            session.getTransaction().commit();
        }
        return calculations;
    }

    @Override
    public void create(Calculation entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Calculation entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.refresh(entity);
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
