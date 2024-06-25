package kettlebell.weather.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import kettlebell.weather.dto.db.Seance;
import kettlebell.weather.exception.SeanceEndedException;
import kettlebell.weather.exception.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeanceRepository extends EntityRepository /*implements BaseDao<Seance>*/ {
    private static final SeanceRepository INSTANCE = new SeanceRepository();

    public static SeanceRepository getInstance() {
        return INSTANCE;
    }

   // @Override
    public void save(Seance seance) {

        executeInTransaction(session -> {
            Optional<Seance> seanceOptional = session.createQuery("from Seance as s where s.user=:user", Seance.class)
                    .setParameter("user", seance.getUser())
                    .uniqueResultOptional();
            if (seanceOptional.isPresent()) {
                session.remove(seanceOptional.get());
                session.flush();
            }
            session.persist(seance);
        });

    }

   // @Override
    public void delete(Seance seance) {

        executeInTransaction(session -> {
            Optional<Seance> seanceOptional = Optional.ofNullable(session.find(Seance.class, seance.getId()));
            seanceOptional.ifPresent(session::remove);
        });

    }

    public Seance updateOrSeanceIsOver(Seance seance) {

        executeInTransaction(session ->
                Optional.ofNullable(session.find(Seance.class, seance.getId()))
                        .map(s -> {
                            s.setTime(seance.getTime());
                            session.merge(s);
                            return s;
                        })
                        .orElseThrow(() -> new SeanceEndedException(Error.of("Your session has ended please authenticate")))

        );
        return executeWithoutTransactionAndReturn(session ->
                session.createQuery("""
                                    select s 
                                    from Seance as s
                                    left join fetch s.user
                                    where  s.id =: key
                                """, Seance.class)
                        .setParameter("key", seance.getId())
                        .getSingleResult()
        );

    }

    public Seance findByIdWithUser(String keySeance) {

        return executeWithoutTransactionAndReturn(session -> {

            return session.createQuery("""
                            select s 
                            from Seance as s
                            left join fetch s.user
                            where  s.id =: key
                            """, Seance.class)
                    .setParameter("key", keySeance)
                    .getSingleResult();

        });

    }

    public Seance findByIdWithUserAndLocations(String keySeance) {


        return executeWithoutTransactionAndReturn(session ->

             session.createQuery("""
                            select s 
                            from Seance as s
                            left join fetch s.user as u
                            left join fetch u.locations
                            where  s.id =: key
                            """, Seance.class)
                    .setParameter("key", keySeance)
                    .getSingleResult()

        );

    }

    public void clearSeance() {

        executeInTransaction(session -> session.createMutationQuery("delete from Seance where time < :time_now")
                .setParameter("time_now", LocalDateTime.now())
                .executeUpdate());

    }

}
