package kettlebell.weather.model.weather.components;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Main {
	private String temp;
	private String feels_like;
	private String pressure;
	private String humidity;
	private String temp_min;
	private String temp_max;
	private String sea_level;
	private String grnd_level;
}
