package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.Calculation;

import java.util.List;
import java.util.Optional;

public class CalculationDAOImpl implements DAO<Calculation> {
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

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

            Query<Calculation> query = session.createQuery("from Calculation obj where obj.hash = :value", Calculation.class);
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
            calculations = session.createQuery("from Calculation obj", Calculation.class).list();
            session.getTransaction().commit();
        }
        return calculations;
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
