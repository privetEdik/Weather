package kettlebell.weather.model.weather;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import kettlebell.weather.model.weather.components.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherModel {
	private Long idLocation;
	private Coord coord;
	@JsonProperty("weather")
	private List<Weather> weatherList;
	private String base;
	private Main main;
	private String visibility;
	private Wind wind;
	private Clouds clouds;
	private Rain rain;
	private Snow snow;
	private String dt;
	private Sys sys;
	private String timezone;
	private String id;
	private String name;
	private String cod;
}
