package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;

import java.util.List;
import java.util.Optional;

public class AppliedFunctionDAOImpl implements DAO<AppliedFunction> {
    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Optional<AppliedFunction> read(int id) {
        AppliedFunction appliedFunction = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            appliedFunction = session.get(AppliedFunction.class, id);
            session.getTransaction().commit();
        }
        return Optional.ofNullable(appliedFunction);
    }

    @Override
    public List<AppliedFunction> readAll() {
        List<AppliedFunction> appliedFunction = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            appliedFunction = session.createQuery("from AppliedFunction", AppliedFunction.class).list();
            session.getTransaction().commit();
        }
        return appliedFunction;
    }

    @Override
    public void create(AppliedFunction entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(AppliedFunction entity) {
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
            session.remove(session.get(AppliedFunction.class, id));
            session.getTransaction().commit();
        }
    }
}
