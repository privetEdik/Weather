package kettlebell.weather.util;

import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    private static SessionFactory sessionFactory;

    private HibernateRunner() {

    }

    public static SessionFactory getInstanceSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Location.class);
            configuration.addAnnotatedClass(Seance.class);

            sessionFactory = configuration.buildSessionFactory();

        }
        return sessionFactory;
    }

    public static SessionFactory getInstanceSessionFactory(Configuration configuration) {
        if (sessionFactory == null || sessionFactory.isClosed()) {

            sessionFactory = configuration.buildSessionFactory();

        }
        return sessionFactory;
    }

}
