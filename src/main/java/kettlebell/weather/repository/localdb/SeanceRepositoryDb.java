package kettlebell.weather.repository.localdb;

import java.time.LocalDateTime;
import java.util.Optional;

import kettlebell.weather.dao.BaseDao;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.exception.SeanceEndedException;
import kettlebell.weather.validator.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public  class SeanceRepositoryDb extends EntityRepositoryDb implements BaseDao<String,Seance> {
	private static final SeanceRepositoryDb INSTANCE = new SeanceRepositoryDb();

	public static SeanceRepositoryDb getInstance() {
		return INSTANCE;
	}
	@Override
	public void save(Seance seance) {

		executeInTransaction(session -> {
			Optional<Seance> seanceOptional = session.createQuery("from Seance as s where s.user=:user",Seance.class)
					.setParameter("user",seance.getUser())
					.uniqueResultOptional();
			if(seanceOptional.isPresent()){
				session.remove(seanceOptional.get());
				session.flush();
			}
			session.persist(seance);
		});

	}
	@Override
	public void delete(Seance seance) {

		executeInTransaction(session -> {
			Optional<Seance> seanceOptional = Optional.ofNullable(session.find(Seance.class,seance.getId()));
			seanceOptional.ifPresent(session::remove);
		} );

	}

	public Seance updateOrSeanceIsOver(Seance seance) {

		return executeInTransactionAndReturn(session ->
					Optional.ofNullable(session.find(Seance.class, seance.getId()))
							.map(s->{
								s.setTime(seance.getTime());
								session.merge(s);
								return s;
							})
							.orElseThrow(()-> new SeanceEndedException(Error.of("Your session has ended please authenticate")))

		);
	}

	public Seance findById(String keySeance) {

		return executeInTransactionAndReturn(session -> session.find(Seance.class,keySeance));

	}

	public void clearSeance() {

		executeInTransaction(session -> session.createMutationQuery("delete from Seance where time < :time_now")
				.setParameter("time_now",LocalDateTime.now())
				.executeUpdate());

	}

}
