package kettlebell.weather.model.weather.components;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coord {
	private String lon;
	private String lat;
}
