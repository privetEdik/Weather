package kettlebell.weather.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
public class UserDto {
	Long id;
	String login;
	List<LocationDto> locationDtos;
	
}
