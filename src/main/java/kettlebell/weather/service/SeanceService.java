package kettlebell.weather.service;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kettlebell.weather.dto.LocationDto;
import kettlebell.weather.dto.UserDto;
import kettlebell.weather.entity.Location;
import kettlebell.weather.entity.Seance;
import kettlebell.weather.entity.User;
import kettlebell.weather.exception.AppException;
import kettlebell.weather.exception.JsonException;
import kettlebell.weather.exception.validator.ValidationException;
import kettlebell.weather.mapper.WeatherModelToLocationDto;
import kettlebell.weather.model.weather.WeatherModel;
import kettlebell.weather.model.weather.components.Coord;
import kettlebell.weather.repository.http.LocationRepositoryHttp;
import kettlebell.weather.repository.localdb.SeanceRepositoryDb;
import kettlebell.weather.repository.localdb.UserRepositoryDb;
import kettlebell.weather.storage.LocationStorageInstance;
import kettlebell.weather.util.PropertiesUtil;
import kettlebell.weather.util.ScheduledExecutorServiceUtil;
import kettlebell.weather.validator.Error;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SeanceService {
    private static final Long SEANCE_LIFETIME = Long.parseLong(PropertiesUtil.getProperty("period_seance"));
    private static final Long CLEANING_PERIOD = Long.parseLong(PropertiesUtil.getProperty("period_cleaning"));
    private static final WeatherModelToLocationDto weatherModelToLocationDto = WeatherModelToLocationDto.getInstance();
    private LocationRepositoryHttp locationRepositoryHttp;
    private SeanceRepositoryDb seanceRepositoryDb;
    private UserRepositoryDb userRepositoryDb;
    private final ObjectMapper mapper = new ObjectMapper();


    public SeanceService(SeanceRepositoryDb seanceRepositoryDb) {
        this.seanceRepositoryDb = seanceRepositoryDb;
    }

    public SeanceService(SeanceRepositoryDb seanceRepositoryDb, UserRepositoryDb userRepositoryDb) {
        this.seanceRepositoryDb = seanceRepositoryDb;
        this.userRepositoryDb = userRepositoryDb;
    }

    public SeanceService(SeanceRepositoryDb seanceRepositoryDb, LocationRepositoryHttp locationRepositoryHttp) {
        this.seanceRepositoryDb = seanceRepositoryDb;
        this.locationRepositoryHttp = locationRepositoryHttp;
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
        LocationStorageInstance.INSTANCE.clearStorage(keySeance);
    }


    public UserDto findUserDtoFromSeance(String keySeance) throws ValidationException {

        User user = seanceRepositoryDb.findById(keySeance).getUser();

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
                    String jsonStringResponse = locationRepositoryHttp.loadingWeatherFromTheOpenWeatherAPI(weatherModel);
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
