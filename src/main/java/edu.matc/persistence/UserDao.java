package edu.matc.persistence;

import edu.matc.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDao {
    private final Logger logger = LogManager.getLogger(this.getClass());
    SessionFactory sessionFactory = SessionFactoryProvider.getSessionFactory();

    public List<User> getAll() {

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery( User.class );
        Root<User> root = query.from(User.class);
        List<User> users = session.createQuery( query ).getResultList();

        logger.debug("The list of users: " + users);
        session.close();

        return users;
    }

    public List<User> getUsersByUserName(String userName) {

        logger.debug("Searching for: {}" , userName);

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Expression<String> propertryPath = root.get("username");
        query.where(builder.like(propertryPath, "%" + userName + "%"));
        List<User> users = session.createQuery(query).getResultList();
        session.close();
        return users;
    }

    public User getUserByUserName(String userName) {

        logger.debug("Searching for: {}" , userName);

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Expression<String> propertryPath = root.get("username");
        query.where(builder.like(propertryPath, userName));
        User user = session.createQuery(query).getSingleResult();
        session.close();
        return user;
    }

    public User getById(int id) {
        Session session = sessionFactory.openSession();
        User user = session.get( User.class, id );
        logger.debug("Returning user by id:" + id + " User: " + user);
        session.close();
        return user;
    }

    public int insert(User user) {
        int id = 0;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        id = (int)session.save(user);
        logger.debug("Inserting new user: " + user);
        transaction.commit();
        session.close();
        return id;
    }

    public void delete(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    public void saveOrUpdate(User user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        logger.debug("Updating user: " + user);
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

}
