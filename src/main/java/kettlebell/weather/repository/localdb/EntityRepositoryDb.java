package kettlebell.weather.repository.localdb;

import kettlebell.weather.util.HibernateRunner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class EntityRepositoryDb {
    private static final SessionFactory sessionFactory = HibernateRunner.getInstanceSessionFactory();

    public void executeInTransaction(Consumer<Session> operation){
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();

            operation.accept(session);
            session.flush();

            session.getTransaction().commit();
        }
    }

    public <T> T executeInTransactionAndReturn(Function<Session, T> operation){
        try(Session session = sessionFactory.openSession()){
            return operation.apply(session);
        }
    }
}
