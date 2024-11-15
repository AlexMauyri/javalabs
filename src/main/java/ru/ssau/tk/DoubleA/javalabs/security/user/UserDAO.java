package ru.ssau.tk.DoubleA.javalabs.security.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDAO {
    private static final SessionFactory sessionFactory = new Configuration().configure().addAnnotatedClass(User.class).buildSessionFactory();
    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    public User findByUsername(String username) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
            session.getTransaction().commit();
            logger.info("Found user: {}", username);
        } catch (HibernateException e) {
            logger.error(e);
        }
        return user;
    }

    public boolean existsByUsername(String username) {
        boolean exists;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            exists = session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult() != null;
        }
        return exists;
    }

    public List<User> findAllUsers() {
        List<User> users = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            users = session.createQuery("from User", User.class).getResultList();
            session.getTransaction().commit();
            logger.info("Get all users");
        } catch (HibernateException e) {
            logger.error(e);
        }
        return users;
    }

    public User getUserById(int id) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.createQuery("from User where user_id = :id", User.class)
                    .setParameter("id", id)
                    .uniqueResult();
            session.getTransaction().commit();
            logger.info("Found user with id: {}", id);
        } catch (HibernateException e) {
            logger.error(e);
        }
        return user;
    }

    public void registerUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            logger.info("New user: {}", user.getUsername());
        } catch (HibernateException e) {
            logger.error(e);
        }
    }

    public User deleteUserById(int id) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.remove(user);
            session.getTransaction().commit();
            logger.info("Deleted user: {}", user.getUsername());
        } catch (HibernateException e) {
            logger.error(e);
        }
        return user;
    }

    public User updateUserRole(int id) {
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            user.setRole((user.getRole() == Role.USER? Role.ADMIN: Role.USER));
            session.merge(user);
            session.getTransaction().commit();
            logger.info("Updated user role: {}", user.getUsername());
        }
        catch (HibernateException e) {
            logger.error(e);
        }
        return user;
    }
}
