package ru.ssau.tk.DoubleA.javalabs.persistence.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import ru.ssau.tk.DoubleA.javalabs.persistence.entity.AppliedFunction;

import java.util.List;

public class AppliedFunctionDAOImpl implements DAO<AppliedFunction> {
    private static final Logger logger = LogManager.getLogger(AppliedFunctionDAOImpl.class);
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public AppliedFunction read(int id) {
        AppliedFunction appliedFunction = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            appliedFunction = session.get(AppliedFunction.class, id);
            session.getTransaction().commit();
            logger.info("Read AppliedFunction with ID: {}", id);
        } catch (HibernateException e) {
            logger.error(e);
        }
        return appliedFunction;
    }

    public List<AppliedFunction> readByCalculationId(int id) {
        List<AppliedFunction> appliedFunction = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<AppliedFunction> query = session.createQuery(loadQueryFromFile("readByCalculationId_AppliedFunction.hql"), AppliedFunction.class);
            query.setParameter("value", id);
            appliedFunction = query.list();

            session.getTransaction().commit();
            logger.info("Read AppliedFunction with Calculation ID: {}", id);
        } catch (HibernateException e) {
            logger.error(e);
        }
        return appliedFunction;
    }

    @Override
    public List<AppliedFunction> readAll() {
        List<AppliedFunction> appliedFunction = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            appliedFunction = session.createQuery(loadQueryFromFile("readAll_AppliedFunction.hql"), AppliedFunction.class).list();
            session.getTransaction().commit();
            logger.info("Read All AppliedFunction");
        } catch (HibernateException e) {
            logger.error(e);
        }
        return appliedFunction;
    }

    @Override
    public void create(AppliedFunction entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            logger.info("Create AppliedFunction with ID: {}", entity.getId());
        } catch (HibernateException e) {
            logger.error(e);
        }
    }

    @Override
    public void update(AppliedFunction entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            logger.info("Update AppliedFunction with ID: {}. New values: {}, {}, {}, {}, {}",
                    entity.getId(),
                    entity.getCalculationId().getId(),
                    entity.getFunctionOrder(),
                    entity.getFunctionSerialized(),
                    entity.getModUnmodifiable(),
                    entity.getModStrict());
        } catch (HibernateException e) {
            logger.error(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.get(AppliedFunction.class, id));
            session.getTransaction().commit();
            logger.info("Delete AppliedFunction with ID: {}", id);
        } catch (HibernateException e) {
            logger.error(e);
        }
    }
}
