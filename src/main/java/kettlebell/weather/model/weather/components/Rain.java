package kettlebell.weather.model.weather.components;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Rain {
	
	private String oneH;
	private String threeH;

    @JsonSetter("1h")
	public void setOneH(String oneH) {

		this.oneH = oneH;
	}

    @JsonSetter("3h")
	public void setThreeH(String threeH) {

		this.threeH = threeH;
	}

}
