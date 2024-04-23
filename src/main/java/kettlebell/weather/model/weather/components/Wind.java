package kettlebell.weather.model.weather.components;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wind {
	private String speed;
	private String deg;
	private String gust;
}
