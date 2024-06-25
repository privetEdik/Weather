package kettlebell.weather.repository;

import kettlebell.weather.dto.db.Location;
import kettlebell.weather.dto.db.User;
import kettlebell.weather.exception.APIException;
import kettlebell.weather.exception.EntityAlreadyExistsException;
import kettlebell.weather.exception.KeyForApiWeatherException;
import kettlebell.weather.dto.api.weather.WeatherModel;
import kettlebell.weather.exception.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationRepository extends EntityRepository {

    private static final String HOST = "https://api.openweathermap.org/";
    private static String KEY;
    private static final String UNITS = "&units=metric";
    private static final String LANG = "&lang=en";
    private final HttpClient client = HttpClient.newHttpClient();
    private String uri;
    private HttpRequest request;

    private static final LocationRepository INSTANCE = new LocationRepository();

    public static LocationRepository getInstance() {
        KEY = System.getenv("KEY_FOR_WEATHER");

        if (KEY==null){
            throw new KeyForApiWeatherException(Error.of("460","API-Weather key not found"));
        }
        return INSTANCE;
    }
   // @Override
    public void save(Location entity) throws EntityAlreadyExistsException {
        try {
            executeInTransaction(session -> {
                User user = session.find(User.class, entity.getUser().getId());

                user.addLocation(entity);

                session.persist(entity);
            });
        } catch (ConstraintViolationException e) {

            throw new EntityAlreadyExistsException(Error.of("Location already exists"));
        }

    }

    //@Override
    public void delete(Location entity) {
        executeInTransaction(session -> {
            Location location = session.find(Location.class, entity.getId());
            session.remove(location);
        });
    }

    //@Override
    public String findJsonLocations(String nameTown) {

        uri = HOST + "geo/1.0/direct?q=" + nameTown + "&limit=5&appid=" + KEY;

        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(uri))
                .timeout(Duration.of(2, ChronoUnit.SECONDS))
                .build();

        try {

            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new APIException(Error.of("500", "Timed out request. Error when loading data on found locations from the OpenWeatherAPI"));
        }
    }

   // @Override
    public String findJsonWeather(WeatherModel model) {

        uri = HOST + "data/2.5/weather?lat=" + model.getCoord().getLat() + "&lon=" + model.getCoord().getLon() + LANG + "&appid=" + KEY + UNITS;

        request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(uri))
                .timeout(Duration.of(2, ChronoUnit.SECONDS))
                .build();

        try {

            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new APIException(Error.of("500", "Timed out request. Error when loading weather data from the OpenWeatherAPI"));
        }
    }
}
