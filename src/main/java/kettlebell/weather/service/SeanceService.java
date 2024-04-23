package kettlebell.weather.service;


import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import kettlebell.weather.dto.UserDto;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.mapper.UserToUserDtoMapper;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.util.PropertiesUtil;
import kettlebell.weather.util.ScheduledExecutorServiceUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeanceService {
	private static final SeanceService INSTANCE = new SeanceService();
	private static final Long SEANCE_LIFETIME = Long.parseLong(PropertiesUtil.getProperty("period_seance"));
	private final UserToUserDtoMapper userToUserDtoMapper = UserToUserDtoMapper.getInstance();
	private final SeanceRepositoryDb seanceRepositoryDb = SeanceRepositoryDb.getInstance();
	private final UserRepositoryDb userRepositoryDb = UserRepositoryDb.getInstance();

	public static SeanceService getInstance() {
		return INSTANCE;
	}

	
	public String startSeanceAndGetKey(Long idUser) {

			User user = userRepositoryDb.findById(idUser);
			String keySeance = UUID.randomUUID().toString();
			LocalDateTime time = LocalDateTime.now().plusMinutes(SEANCE_LIFETIME);
			
			Seance seance = Seance.builder()
					.id(keySeance)
					.user(user)
					.time(time)
					.build();
			
			seanceRepositoryDb.save(seance);

			return keySeance;
	}
	
	public void deleteExpiredSeances(){
		ScheduledExecutorService ses =  ScheduledExecutorServiceUtil.getInstance();
		ses.schedule(new SeanceObserver(), 2, TimeUnit.MINUTES);
	}

	public void forceDelete(String keySeance){

		seanceRepositoryDb.delete(Seance.builder().id(keySeance).build());
	}

	public UserDto findUserDtoFromSeance(String keySeance) throws ValidationException {

		Seance seance = seanceRepositoryDb.findById(keySeance);
		
		return userToUserDtoMapper.mapFrom(seance.getUser());
	}

	public String updateSeance(String keySeance) throws AppException {
		Seance seance = Seance.builder()
				.time(LocalDateTime.now().plusMinutes(SEANCE_LIFETIME))
				.id(keySeance)
				.build();

		return seanceRepositoryDb.updateOrSeanceIsOver(seance).getUser().getLogin();

	}
}
