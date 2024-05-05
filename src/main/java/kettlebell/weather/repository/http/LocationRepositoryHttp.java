package kettlebell.weather.repository.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import kettlebell.weather.exception.APIException;
import kettlebell.weather.model.weather.WeatherModel;
import kettlebell.weather.validator.Error;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationRepositoryHttp {
    private static final String HOST = "https://api.openweathermap.org/";
    private static final String KEY = System.getenv("KEY_FOR_WEATHER");
    private static final String UNITS = "&units=metric";
    private static final String LANG = "&lang=en";
    private final HttpClient client = HttpClient.newHttpClient();
    private String uri;
    private HttpRequest request;


    private static final LocationRepositoryHttp INSTANCE = new LocationRepositoryHttp();

    public static LocationRepositoryHttp getInstance() {
        return INSTANCE;
    }

    public String loadingLocationsFromTheOpenWeatherAPI(String nameTown) {

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

    public String loadingWeatherFromTheOpenWeatherAPI(WeatherModel model) {

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
