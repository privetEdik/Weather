package kettlebell.weather.util;

import kettlebell.weather.dto.db.Location;
import kettlebell.weather.dto.db.Seance;
import kettlebell.weather.dto.db.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class HibernatePropertiesFactory {
    private static SessionFactory sessionFactory;

    private HibernatePropertiesFactory() {

    }

    public static SessionFactory getInstanceSessionFactory() {

        if (sessionFactory == null) {

            String dialect = System.getenv("HIBERNATE_DIALECT");
            String driver = System.getenv("HIBERNATE_DRIVER");
            String url = System.getenv("HIBERNATE_URL");
            String username = System.getenv("HIBERNATE_USERNAME");
            String password = System.getenv("HIBERNATE_PASSWORD");
            String ddl = System.getenv("HIBERNATE_HB2DDL");

            Properties properties = new Properties();
            properties.put(Environment.DRIVER, driver);
            properties.put(Environment.URL, url);
            properties.put(Environment.USER, username);
            properties.put(Environment.PASS, password);
            properties.put(Environment.DIALECT, dialect);
            properties.put(Environment.HBM2DDL_AUTO, ddl);

            Configuration configuration = new Configuration();


            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Location.class);
            configuration.addAnnotatedClass(Seance.class);
            configuration.setProperties(properties);

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
