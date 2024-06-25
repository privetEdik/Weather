package kettlebell.weather.repository;

import kettlebell.weather.util.HibernatePropertiesFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class EntityRepository {
    private static final SessionFactory sessionFactory = HibernatePropertiesFactory.getInstanceSessionFactory();

    public void executeInTransaction(Consumer<Session> operation) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            operation.accept(session);
            session.flush();

            session.getTransaction().commit();
        }
    }

    public <T> T executeWithoutTransactionAndReturn(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            return operation.apply(session);
        }
    }
}
