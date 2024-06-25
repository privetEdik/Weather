package kettlebell.weather.service;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kettlebell.weather.dto.user.LocationDto;
import kettlebell.weather.dto.user.UserDto;
import kettlebell.weather.dto.db.Location;
import kettlebell.weather.dto.db.Seance;
import kettlebell.weather.dto.db.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.JsonException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.mapper.WeatherModelToLocationDto;
import kettlebell.weather.dto.api.weather.WeatherModel;
import kettlebell.weather.dto.api.weather.components.Coord;
import kettlebell.weather.repository.LocationRepository;
import kettlebell.weather.repository.SeanceRepository;
import kettlebell.weather.repository.UserRepository;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.util.PropertiesUtil;
import kettlebell.weather.util.ScheduledExecutorServiceUtil;
import kettlebell.weather.exception.Error;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SeanceService {
    private static final Long SEANCE_LIFETIME = Long.parseLong(PropertiesUtil.getProperty("period_seance"));
    private static final Long CLEANING_PERIOD = Long.parseLong(PropertiesUtil.getProperty("period_cleaning"));
    private static final WeatherModelToLocationDto weatherModelToLocationDto = WeatherModelToLocationDto.getInstance();
    private LocationRepository locationRepository;
    private SeanceRepository seanceRepositoryDb;
    private UserRepository userRepositoryDb;
    private final ObjectMapper mapper = new ObjectMapper();


    public SeanceService(SeanceRepository seanceRepositoryDb) {
        this.seanceRepositoryDb = seanceRepositoryDb;
    }

    public SeanceService(SeanceRepository seanceRepositoryDb, UserRepository userRepositoryDb) {
        this.seanceRepositoryDb = seanceRepositoryDb;
        this.userRepositoryDb = userRepositoryDb;
    }

    public SeanceService(SeanceRepository seanceRepositoryDb, LocationRepository locationRepository) {
        this.seanceRepositoryDb = seanceRepositoryDb;
        this.locationRepository = locationRepository;
    }

    public String startSeanceAndGetKey(Long idUser) {

        User user = userRepositoryDb.findById(idUser);
        String keySeance = UUID.randomUUID().toString();
        LocalDateTime time = LocalDateTime.now().plusSeconds(SEANCE_LIFETIME);

        Seance seance = Seance.builder()
                .id(keySeance)
                .user(user)
                .time(time)
                .build();

        seanceRepositoryDb.save(seance);

        return keySeance;
    }

    public void deleteExpiredSeances() {
        ScheduledExecutorServiceUtil.getInstance().scheduleAtFixedRate(new SeanceObserver(), SEANCE_LIFETIME, CLEANING_PERIOD, TimeUnit.SECONDS);
    }

    public void forceDelete(String keySeance) {

        seanceRepositoryDb.delete(Seance.builder().id(keySeance).build());
        LocationStorageInstance.clearStorage(keySeance);
    }


    public UserDto findUserDtoFromSeance(String keySeance) throws ValidationException {

        User user = seanceRepositoryDb.findByIdWithUserAndLocations(keySeance).getUser();

        List<LocationDto> loc = user.getLocations().stream()
                .sorted(Comparator.comparingLong(Location::getId))
                .map(location -> {
                    WeatherModel model = new WeatherModel();
                    model.setIdLocation(location.getId());
                    model.setCoord(
                            Coord.builder()
                                    .lat(location.getLatitude().toString())
                                    .lon(location.getLongitude().toString())
                                    .build()
                    );
                    model.setName(location.getName());
                    return model;
                })
                .map(weatherModel -> {
                    String jsonStringResponse = locationRepository.findJsonWeather(weatherModel);
                    try {
                        WeatherModel modelResponse = mapper.readValue(jsonStringResponse, WeatherModel.class);
                        modelResponse.setIdLocation(weatherModel.getIdLocation());
                        modelResponse.setName(weatherModel.getName());
                        return modelResponse;
                    } catch (JsonProcessingException e) {
                        throw new JsonException(Error.of(e.getMessage()));
                    }

                })
                .map(weatherModelToLocationDto::mapFrom)
                .toList();

        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .locationDtos(loc)
                .build();
    }

    public String updateSeance(String keySeance) throws AppException {
        Seance seance = Seance.builder()
                .time(LocalDateTime.now().plusSeconds(SEANCE_LIFETIME))
                .id(keySeance)
                .build();

        return seanceRepositoryDb.updateOrSeanceIsOver(seance).getUser().getLogin();

    }
}
