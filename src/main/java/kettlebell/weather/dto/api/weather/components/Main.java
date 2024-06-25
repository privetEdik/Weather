package kettlebell.weather.dto.api.weather.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Main {
	private String temp;
	@JsonProperty("feels_like")
	private String feelsLike;
	private String pressure;
	private String humidity;
	@JsonProperty("temp_min")
	private String tempMin;
	@JsonProperty("temp_max")
	private String tempMax;
	@JsonProperty("sea_level")
	private String seaLevel;
	@JsonProperty("grnd_level")
	private String grndLevel;
}
