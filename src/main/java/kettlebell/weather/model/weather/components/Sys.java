package kettlebell.weather.model.weather.components;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sys {
	private String type;
	private String id;
	private String message;
	private String country;
	private String sunrise;
	private String sunset;
}
