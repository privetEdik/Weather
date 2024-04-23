package kettlebell.weather.model.weather.components;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
	private String id;
	private String main;
	private String description;
	private String icon;
}
