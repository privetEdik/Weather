package kettlebell.weather.model.weather.components;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Rain {
	
	private String oneH;
	private String threeH;
	
	public String getOneH() {
		return oneH;
	}
	@JsonSetter("1h")
	public void setOneH(String oneH) {
		this.oneH = oneH;
	}

	public String getThreeH() {
		return threeH;
	}
	@JsonSetter("3h")
	public void setThreeH(String threeH) {
		this.threeH = threeH;
	}

}
