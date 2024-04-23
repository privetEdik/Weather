package kettlebell.weather.repository.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kettlebell.weather.exception.APIException;
import kettlebell.weather.model.TownModel;
import kettlebell.weather.model.weather.WeatherModel;
import kettlebell.weather.service.LocationService;
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
	private static final HttpClient client = HttpClient.newHttpClient();
	private static final ObjectMapper mapper = new ObjectMapper();
	private String uri;
	private HttpRequest request;


	private static final LocationRepositoryHttp INSTANCE = new LocationRepositoryHttp();

	public static LocationRepositoryHttp getInstance() {
		return INSTANCE;
	}

	public List<TownModel> loadingLocationsFromTheOpenWeatherAPI(String nameTown) {
		List<TownModel> listTown;

		uri = HOST+"geo/1.0/direct?q="+ nameTown+"&limit=5&appid="+KEY;

		request = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
					.uri(URI.create(uri))
					.timeout(Duration.of(2,ChronoUnit.SECONDS))
					.build();

		try {
			String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

			listTown = mapper.readValue(responseBody, new TypeReference<List<TownModel>>(){});

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new APIException(Error.of("Error when loading data on found locations from the OpenWeatherAPI"));
		}


		return listTown;
	
	}
	public WeatherModel loadingWeatherFromTheOpenWeatherAPI(WeatherModel model){

		uri = HOST + "data/2.5/weather?lat=" + model.getCoord().getLat() + "&lon=" + model.getCoord().getLon() + LANG + "&appid=" + KEY + UNITS;

		WeatherModel modelResponse;
		
		 request = HttpRequest.newBuilder()
					.version(HttpClient.Version.HTTP_2)
					.uri( URI.create(uri))
					.timeout(Duration.of(2,ChronoUnit.SECONDS))
					.build();

			try {
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				 modelResponse  = mapper.readValue(response.body(), WeatherModel.class);
				 modelResponse.setIdLocation(model.getIdLocation());
				 modelResponse.setName(model.getName());

			} catch (IOException | InterruptedException e) {
				log.error(e.getMessage());
				throw new APIException(Error.of("Error when loading weather data from the OpenWeatherAPI"));
			}

		return modelResponse;	
	}
}
